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
    var scrollView: UIScrollView!
    var contentView: UIView!
    
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
                vc.drinkTableView.invalidateIntrinsicContentSize()
                vc.updateTableViewToWrapHeight()
                vc.updateScrollViewHeight()
            }
        },
        onNewToastMessage: { [weak self] displayValue in
            if let vc = self {
                vc.showToast(message: displayValue)
            }
        }
        )
    
    override func viewWillDisappear(_ animated: Bool) {
        viewModel.onDestroy()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        viewModel.observeUpdates()
    }
    
    override func viewDidLoad() {
        scrollView = UIScrollView()
        scrollView.isScrollEnabled = true
        scrollView.translatesAutoresizingMaskIntoConstraints = false
        contentView = UIView()
        contentView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(scrollView)
        
        scrollView.addSubview(contentView)
        scrollView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor).isActive = true
        scrollView.rightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.rightAnchor).isActive = true
        scrollView.leftAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leftAnchor).isActive = true
        scrollView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor).isActive = true
        scrollView.widthAnchor.constraint(equalTo: view.safeAreaLayoutGuide.widthAnchor).isActive = true
        scrollView.heightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.heightAnchor).isActive = true
        
        contentView.topAnchor.constraint(equalTo: scrollView.topAnchor).isActive = true
        contentView.rightAnchor.constraint(equalTo: scrollView.rightAnchor).isActive = true
        contentView.leftAnchor.constraint(equalTo: scrollView.leftAnchor).isActive = true
        contentView.widthAnchor.constraint(equalTo: scrollView.widthAnchor).isActive = true
        contentView.heightAnchor.constraint(greaterThanOrEqualTo: scrollView.heightAnchor, constant: 1000).isActive = true
        
        wantedBloodLevelView = WantedBloodLevelView()
        wantedBloodLevelView.onSliderChanged = self
        hoursDrinkingView = HoursDrinkingView()
        hoursDrinkingView.onSliderChanged = self
        setupTableView()
        
        contentView.addSubview(wantedBloodLevelView)
        contentView.addSubview(hoursDrinkingView)
        contentView.addSubview(drinkTableView)
        arrangeViews()
    }
    
    func setupTableView() {
        drinkTableView = UITableView()
        drinkTableView.translatesAutoresizingMaskIntoConstraints = false
        
        let headerView: UIView = UIView.init(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 30))
        let labelView: UILabel = UILabel.init(frame: CGRect(x: 32, y: 5, width: view.frame.width, height: 24))
        labelView.text = "What will you be drinking?"
        labelView.font = .systemFont(ofSize: 16)
        headerView.addSubview(labelView)
        
        let footerView = UIView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 128))
        let startDrinkingButton = UIButton(frame: CGRect(x: 0, y: 0, width: 400, height: 84))
        footerView.addSubview(startDrinkingButton)
        startDrinkingButton.translatesAutoresizingMaskIntoConstraints = false
        startDrinkingButton.setTitle("Start drinking", for: .normal)
        startDrinkingButton.backgroundColor = UIColor(named: "Green")
        startDrinkingButton.titleLabel?.sizeToFit()
        startDrinkingButton.layer.cornerRadius = 10
        startDrinkingButton.centerXAnchor.constraint(equalTo: footerView.centerXAnchor).isActive = true
        startDrinkingButton.centerYAnchor.constraint(equalTo: footerView.centerYAnchor).isActive = true
        startDrinkingButton.addTarget(self, action: #selector(startDrinkingPressed), for: .touchUpInside)
        
        drinkTableView.tableFooterView = footerView
        drinkTableView.rowHeight = 64
        drinkTableView.estimatedRowHeight = 64
        drinkTableView.tableHeaderView = headerView
        drinkTableView.isScrollEnabled = false
        drinkTableView.register(DrinkItemViewCell.self, forCellReuseIdentifier: "MyCell")
        drinkTableView.dataSource = self
        drinkTableView.delegate = self
    }
    
    func arrangeViews() {
        
        wantedBloodLevelView.translatesAutoresizingMaskIntoConstraints = false
        wantedBloodLevelView.topAnchor.constraint(equalTo: contentView.safeAreaLayoutGuide.topAnchor).isActive = true
        wantedBloodLevelView.centerXAnchor.constraint(equalTo: contentView.centerXAnchor).isActive = true
        wantedBloodLevelView.widthAnchor.constraint(equalTo: contentView.widthAnchor).isActive = true
        
        hoursDrinkingView.topAnchor.constraint(equalTo: wantedBloodLevelView.bottomAnchor, constant: 16).isActive = true
        hoursDrinkingView.centerXAnchor.constraint(equalTo: contentView.centerXAnchor).isActive = true
        hoursDrinkingView.widthAnchor.constraint(equalTo: contentView.widthAnchor).isActive = true
        
        drinkTableView.topAnchor.constraint(equalTo: hoursDrinkingView.bottomAnchor, constant: 8).isActive = true
        drinkTableView.rightAnchor.constraint(equalTo: contentView.rightAnchor).isActive = true
        drinkTableView.widthAnchor.constraint(equalTo: contentView.widthAnchor).isActive = true
        drinkTableView.leftAnchor.constraint(equalTo: contentView.leftAnchor).isActive = true
        drinkTableView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor).isActive = true
        
        updateTableViewToWrapHeight()
        updateScrollViewHeight()
        
    }
    
    func updateTableViewToWrapHeight() {
        var frame = drinkTableView.frame
        frame.size.height = drinkTableView.contentSize.height
        drinkTableView.frame = frame
    }
    
    func updateScrollViewHeight() {
        scrollView.contentSize = CGSize(width: UIScreen.main.bounds.width, height: wantedBloodLevelView.frame.height + hoursDrinkingView.frame.height + drinkTableView.frame.height)
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
    
    @objc func startDrinkingPressed() {
        viewModel.startDrinking()
    }
}

protocol OnSliderChanged {
    func sliderChanged(_ tag: Int, _ value: Float)
}
