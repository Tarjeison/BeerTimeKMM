//
//  MyDrinksViewController.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 07/09/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

class MyDrinksViewController: UIViewController {

    private var drinkArray: Array<MyDrinkItem> = []
    
    lazy var viewModel = NativeMyDrinksViewModel { [weak self] myDrinks in
        if let vc = self {
            vc.drinkArray = myDrinks
            vc.drinkTableView.reloadData()
        }
    }
    
    lazy var drinkTableView: UITableView = {
        let drinkTableView = UITableView()
        drinkTableView.translatesAutoresizingMaskIntoConstraints = false
        
        let footerView = UIView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 128))
        let addDrinkButton = UIButton(frame: CGRect(x: 0, y: 0, width: 400, height: 84))
        footerView.addSubview(addDrinkButton)
        addDrinkButton.translatesAutoresizingMaskIntoConstraints = false
        addDrinkButton.setTitle("Add drink", for: .normal)
        addDrinkButton.backgroundColor = UIColor(named: "Green")
        addDrinkButton.titleLabel?.sizeToFit()
        addDrinkButton.layer.cornerRadius = 10
        addDrinkButton.centerXAnchor.constraint(equalTo: footerView.centerXAnchor).isActive = true
        addDrinkButton.centerYAnchor.constraint(equalTo: footerView.centerYAnchor).isActive = true
        addDrinkButton.contentEdgeInsets = UIEdgeInsets.init(top: 10,left: 10,bottom: 10,right: 10)
        addDrinkButton.addTarget(self, action: #selector(onAddDrinkPressed), for: .touchUpInside)
        drinkTableView.tableFooterView = footerView
        drinkTableView.rowHeight = 64
        drinkTableView.estimatedRowHeight = 64
        drinkTableView.register(MyDrinkItemCell.self, forCellReuseIdentifier: "MyCell")
        drinkTableView.dataSource = self
        drinkTableView.delegate = self
        return drinkTableView
    }()
    
    @objc func onAddDrinkPressed() {
        navigationController?.pushViewController(AddDrinkViewController(), animated: false)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        viewModel.observeDrinks()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        viewModel.onDestroy()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.addSubview(drinkTableView)
        drinkTableView.rightAnchor.constraint(equalTo: view.rightAnchor).isActive = true
        drinkTableView.leftAnchor.constraint(equalTo: view.leftAnchor).isActive = true
        drinkTableView.heightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.heightAnchor).isActive = true
        viewModel.observeDrinks()
    }
}

extension MyDrinksViewController: UITableViewDelegate {
    private func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {}
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            viewModel.deleteDrink(drinkKey: drinkArray[indexPath.row].key)
            drinkArray.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return drinkArray.count
    }
    
    
}

extension  MyDrinksViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyCell", for: indexPath as IndexPath) as! MyDrinkItemCell
        cell.drinkName.text = drinkArray[indexPath.row].name
        cell.drinkDescription.text = drinkArray[indexPath.row].getDescription()
        cell.drinkIcon.image = UIImage(named: drinkArray[indexPath.row].iconName)
        cell.tag = Int(drinkArray[indexPath.row].key)
        return cell
    }
}

