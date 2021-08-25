//
//  StartDrinkingViewController.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 22/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

class StartDrinkingViewController : UIViewController, UITableViewDelegate, UITableViewDataSource, OnSliderChanged {
    
    var wantedBloodLevelView: WantedBloodLevelView!
    var hoursDrinkingView: HoursDrinkingView!
    
    private var drinkArray: Array<AlcoholUnit> = []
    private var drinkTableView: UITableView!
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        viewModel.updateSelectedUnit(alcoholUnit: drinkArray[indexPath.row])
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return drinkArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyCell", for: indexPath as IndexPath) as! DrinkItemViewCell
        cell.drinkName.text = drinkArray[indexPath.row].name
        cell.drinkDescription.text = drinkArray[indexPath.row].getDescription()
        if (drinkArray[indexPath.row].isSelected) {
            cell.backgroundColor = .lightGray
        } else {
            cell.backgroundColor = .clear
        }
        cell.drinkIcon.image = UIImage(named: drinkArray[indexPath.row].iconName)
        return cell
    }
    
    
    lazy var viewModel = NativeStartDrinkingViewModel(
        onWantedBloodLevelTextChanged: { [weak self] displayValue in
            if let vc = self {
                vc.wantedBloodLevelView.wantedBloodLevelSelectedValue.text = displayValue
            }
        },
        onDrinkUntilDisplayTextChanged: { [weak self] displayValue in
            if let vc = self {
                vc.hoursDrinkingView.hoursDrinkingSelectedValue.text = displayValue
            }
        },
        onPeakHourDisplayTextChanged: { [weak self] displayValue in
            if let vc = self {
                vc.hoursDrinkingView.peakHourDisplayText.text = displayValue
            }
        },
        onDrinkListChanged: { [weak self] drinkList in
            if let vc = self {
                vc.drinkArray = drinkList
                vc.drinkTableView.reloadData()
                vc.updateTableViewToWrapHeight()
            }
        })
    
    override func viewWillDisappear(_ animated: Bool) {
        viewModel.onDestroy()
    }
    
    override func viewDidLoad() {
        print("init")
        
        wantedBloodLevelView = WantedBloodLevelView()
        wantedBloodLevelView.onSliderChanged = self
        hoursDrinkingView = HoursDrinkingView()
        hoursDrinkingView.onSliderChanged = self
        setupTableView()
        
        view.addSubview(wantedBloodLevelView)
        view.addSubview(hoursDrinkingView)
        view.addSubview(drinkTableView)
        arrangeViews()
        
        viewModel.observeUpdates()
    }
    
    func setupTableView() {
        drinkTableView = UITableView()
        let headerView: UIView = UIView.init(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 30))
        let labelView: UILabel = UILabel.init(frame: CGRect(x: 32, y: 5, width: view.frame.width, height: 24))
        labelView.text = "What will you be drinking?"
        labelView.font = .systemFont(ofSize: 16)
        headerView.addSubview(labelView)
        
        drinkTableView.tableHeaderView = headerView
        drinkTableView.isScrollEnabled = false
        drinkTableView.translatesAutoresizingMaskIntoConstraints = false
        drinkTableView.register(DrinkItemViewCell.self, forCellReuseIdentifier: "MyCell")
        drinkTableView.dataSource = self
        drinkTableView.delegate = self
    }
    
    func arrangeViews() {
        
        wantedBloodLevelView.translatesAutoresizingMaskIntoConstraints = false
        wantedBloodLevelView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor).isActive = true
        wantedBloodLevelView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        wantedBloodLevelView.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        
        hoursDrinkingView.topAnchor.constraint(equalTo: wantedBloodLevelView.bottomAnchor, constant: 16).isActive = true
        hoursDrinkingView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        hoursDrinkingView.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        
        drinkTableView.topAnchor.constraint(equalTo: hoursDrinkingView.bottomAnchor, constant: 8).isActive = true
        drinkTableView.bottomAnchor.constraint(equalTo: view.bottomAnchor).isActive = true
        drinkTableView.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        
        updateTableViewToWrapHeight()
        
    }
    
    func updateTableViewToWrapHeight() {
        var frame = drinkTableView.frame
        frame.size.height = drinkTableView.contentSize.height
        drinkTableView.frame = frame
    }
    
    func sliderChanged(_ tag: Int, _ value: Float) {
        switch tag {
        case _wantedBloodLevelSliderTag:
            viewModel.updatedSelectedBloodLevel(seekbarValue: Int32(value))
        case _hoursDrinkingSliderTag:
            viewModel.updateFinishDrinking(seekbarValue: Int32(value))
            hoursDrinkingView.peakHourSlider.maximumValue = value
            viewModel.updatePeakHour(seekbarValue: Int32(hoursDrinkingView.peakHourSlider.value))
        case _peakHourSliderTag:
            viewModel.updatePeakHour(seekbarValue: Int32(value))
        default:
            break
            
        }
    }
}

protocol OnSliderChanged {
    func sliderChanged(_ tag: Int, _ value: Float)
}
