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

class StartDrinkingViewController : UIViewController {
    let imageSize: CGFloat = 62
    let wantedBloodLevelSliderTag = 1
    let hoursDrinkingSliderTag = 2

    
    var wantedBloodLevelView: WantedBloodLevelView!
    
    var hoursDrinkingImageView: UIImageView!
    var hoursDrinkingSlider: UISlider!
    var hoursDrinkingText: UITextView!
    var hoursDrinkingSelectedValue: UITextView!
    
    lazy var viewModel = NativeStartDrinkingViewModel(
        onWantedBloodLevelTextChanged: { [weak self] displayValue in
            if let vc = self {
                vc.wantedBloodLevelView.updateText(text: displayValue)
            }
        },
        onDrinkUntilDisplayTextChanged: { displayValue in
            self.hoursDrinkingSelectedValue.text = displayValue
        })
    
    override func viewWillDisappear(_ animated: Bool) {
        viewModel.onDestroy()
    }
    
    override func viewDidLoad() {
        print("init")

        wantedBloodLevelView = WantedBloodLevelView()
        wantedBloodLevelView.onSliderChanged = { [weak self] (tag, value) in
            if let vc = self {
                vc.updateViewModelOnSliderChanged(tag: tag, value: value)
            }
        }
        hoursDrinkingImageView = UIImageView()
        hoursDrinkingSlider = UISlider()
        hoursDrinkingText = UITextView()
        hoursDrinkingSelectedValue = UITextView()
        
        view.addSubview(wantedBloodLevelView)
        
        view.addSubview(hoursDrinkingImageView)
        view.addSubview(hoursDrinkingSlider)
        view.addSubview(hoursDrinkingText)
        view.addSubview(hoursDrinkingSelectedValue)
        
        wantedBloodLevelView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor).isActive = true
        wantedBloodLevelView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        wantedBloodLevelView.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        wantedBloodLevelView.translatesAutoresizingMaskIntoConstraints = false
        wantedBloodLevelView.layoutIfNeeded()

        initHoursDrinkingViews()
        viewModel.observeUpdates()
    }

    
    func initHoursDrinkingViews() {
        if UIImage(named: "drunk") != nil {
            hoursDrinkingImageView.image = UIImage(named: "reading")
            print("Image found")
            hoursDrinkingImageView.contentMode = .scaleAspectFit
            hoursDrinkingImageView.translatesAutoresizingMaskIntoConstraints = false
        }
        
        hoursDrinkingImageView.widthAnchor.constraint(equalToConstant: imageSize).isActive = true
        hoursDrinkingImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        hoursDrinkingImageView.heightAnchor.constraint(equalToConstant: imageSize).isActive = true
        hoursDrinkingImageView.topAnchor.constraint(equalTo: wantedBloodLevelView.bottomAnchor, constant: 16).isActive = true
        
        hoursDrinkingSlider.translatesAutoresizingMaskIntoConstraints = false
        hoursDrinkingSlider.maximumValue = 720
        hoursDrinkingSlider.minimumValue = 0
        hoursDrinkingSlider.thumbTintColor = UIColor(named: "Green")
        hoursDrinkingSlider.topAnchor.constraint(equalTo: hoursDrinkingImageView.bottomAnchor, constant: 16).isActive = true
        hoursDrinkingSlider.widthAnchor.constraint(equalTo: view.safeAreaLayoutGuide.widthAnchor, multiplier: 0.9).isActive = true
        hoursDrinkingSlider.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        hoursDrinkingSlider.tag = hoursDrinkingSliderTag
        hoursDrinkingSlider.addTarget(self, action: #selector(sliderValueChanged), for: UIControl.Event.valueChanged)
        
        hoursDrinkingText.translatesAutoresizingMaskIntoConstraints = false
        hoursDrinkingText.font = .systemFont(ofSize: 16)
        hoursDrinkingText.text = "How long will you be drinking?"
        hoursDrinkingText.sizeToFit()
        hoursDrinkingText.isScrollEnabled = false
        hoursDrinkingText.leftAnchor.constraint(equalTo: hoursDrinkingSlider.leftAnchor).isActive = true
        hoursDrinkingText.topAnchor.constraint(equalTo: hoursDrinkingSlider.bottomAnchor, constant: 4).isActive = true
        
        hoursDrinkingSelectedValue.translatesAutoresizingMaskIntoConstraints = false
        hoursDrinkingSelectedValue.font = .systemFont(ofSize: 16)
        hoursDrinkingSelectedValue.text = ""
        hoursDrinkingSelectedValue.sizeToFit()
        hoursDrinkingSelectedValue.isScrollEnabled = false
        hoursDrinkingSelectedValue.bottomAnchor.constraint(equalTo: hoursDrinkingSlider.topAnchor, constant: 4).isActive = true
        hoursDrinkingSelectedValue.rightAnchor.constraint(equalTo: hoursDrinkingSlider.rightAnchor).isActive = true
        
        
    }
    
    func updateViewModelOnSliderChanged(tag: Int, value: Float){
        switch tag {
        case wantedBloodLevelSliderTag:
            viewModel.updatedSelectedBloodLevel(seekbarValue: Int32(value))
        case hoursDrinkingSliderTag:
            viewModel.updateFinishDrinking(seekbarValue: Int32(value))
        default:
            break
            
        }
    }
    
    @objc func sliderValueChanged(sender: UISlider){
        switch sender.tag {
        case wantedBloodLevelSliderTag:
            viewModel.updatedSelectedBloodLevel(seekbarValue: Int32(sender.value))
        case hoursDrinkingSliderTag:
            viewModel.updateFinishDrinking(seekbarValue: Int32(sender.value))
        default:
            break
            
        }
    }
}
