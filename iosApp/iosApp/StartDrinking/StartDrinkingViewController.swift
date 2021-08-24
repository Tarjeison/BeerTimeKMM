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

class StartDrinkingViewController : UIViewController, OnSliderChanged {
    
    let imageSize: CGFloat = 62
    var wantedBloodLevelView: WantedBloodLevelView!
    var hoursDrinkingView: HoursDrinkingView!
    
    
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
        
        view.addSubview(wantedBloodLevelView)
        view.addSubview(hoursDrinkingView)
        arrangeViews()
        
        viewModel.observeUpdates()
    }
    
    func arrangeViews() {
        
        wantedBloodLevelView.translatesAutoresizingMaskIntoConstraints = false
        wantedBloodLevelView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor).isActive = true
        wantedBloodLevelView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        wantedBloodLevelView.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        
        hoursDrinkingView.topAnchor.constraint(equalTo: wantedBloodLevelView.bottomAnchor, constant: 16).isActive = true
        hoursDrinkingView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        hoursDrinkingView.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        
    }
    
    func sliderChanged(_ tag: Int, _ value: Float) {
        switch tag {
        case _wantedBloodLevelSliderTag:
            viewModel.updatedSelectedBloodLevel(seekbarValue: Int32(value))
        case _hoursDrinkingSliderTag:
            viewModel.updateFinishDrinking(seekbarValue: Int32(value))
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
