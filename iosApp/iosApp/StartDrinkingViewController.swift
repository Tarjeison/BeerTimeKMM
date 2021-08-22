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

    
    var wantedBloodLevelImageView: UIImageView!
    var wantedBloodLevelSlider: UISlider!
    var wantedBloodLevelText: UITextView!
    var wantedBloodLevelSelectedValue: UITextView!
    
    var hoursDrinkingImageView: UIImageView!
    var hoursDrinkingSlider: UISlider!
    var hoursDrinkingText: UITextView!
    var hoursDrinkingSelectedValue: UITextView!
    
    lazy var viewModel = NativeStartDrinkingViewModel(
        onWantedBloodLevelTextChanged: { displayValue in
            self.wantedBloodLevelSelectedValue.text = displayValue
        },
        onDrinkUntilDisplayTextChanged: { displayValue in
            self.hoursDrinkingSelectedValue.text = displayValue
        })
    
    override func viewWillDisappear(_ animated: Bool) {
        viewModel.onDestroy()
    }
    
    override func viewDidLoad() {
        print("init")

        wantedBloodLevelImageView = UIImageView()
        wantedBloodLevelSlider = UISlider()
        wantedBloodLevelText = UITextView()
        wantedBloodLevelSelectedValue = UITextView()
        hoursDrinkingImageView = UIImageView()
        hoursDrinkingSlider = UISlider()
        hoursDrinkingText = UITextView()
        hoursDrinkingSelectedValue = UITextView()
        
        view.addSubview(wantedBloodLevelSlider)
        view.addSubview(wantedBloodLevelText)
        view.addSubview(wantedBloodLevelImageView)
        view.addSubview(wantedBloodLevelSelectedValue)
        
        view.addSubview(hoursDrinkingImageView)
        view.addSubview(hoursDrinkingSlider)
        view.addSubview(hoursDrinkingText)
        view.addSubview(hoursDrinkingSelectedValue)
        
        initWantedBloodLevelViews()
        initHoursDrinkingViews()
        viewModel.observeUpdates()
    }
    
    func initWantedBloodLevelViews() {
        if let image = UIImage(named: "reading") {
            wantedBloodLevelImageView.image = image
            print("Image found")
            wantedBloodLevelImageView.contentMode = .scaleAspectFit
            wantedBloodLevelImageView.translatesAutoresizingMaskIntoConstraints = false
        }
        
        wantedBloodLevelImageView.widthAnchor.constraint(equalToConstant: imageSize).isActive = true
        wantedBloodLevelImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        wantedBloodLevelImageView.heightAnchor.constraint(equalToConstant: imageSize).isActive = true
        wantedBloodLevelImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 10).isActive = true
        
        wantedBloodLevelSlider.translatesAutoresizingMaskIntoConstraints = false
        wantedBloodLevelSlider.maximumValue = 15
        wantedBloodLevelSlider.minimumValue = 0
        wantedBloodLevelSlider.thumbTintColor = UIColor(named: "Green")
        wantedBloodLevelSlider.topAnchor.constraint(equalTo: wantedBloodLevelImageView.bottomAnchor, constant: 16).isActive = true
        wantedBloodLevelSlider.widthAnchor.constraint(equalTo: view.safeAreaLayoutGuide.widthAnchor, multiplier: 0.9).isActive = true
        wantedBloodLevelSlider.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        wantedBloodLevelSlider.tag = wantedBloodLevelSliderTag
        wantedBloodLevelSlider.addTarget(self, action: #selector(sliderValueChanged), for: UIControl.Event.valueChanged)
        
        wantedBloodLevelText.translatesAutoresizingMaskIntoConstraints = false
        wantedBloodLevelText.font = .systemFont(ofSize: 16)
        wantedBloodLevelText.text = "Wanted blood level"
        wantedBloodLevelText.sizeToFit()
        wantedBloodLevelText.isScrollEnabled = false
        wantedBloodLevelText.leftAnchor.constraint(equalTo: wantedBloodLevelSlider.leftAnchor).isActive = true
        wantedBloodLevelText.topAnchor.constraint(equalTo: wantedBloodLevelSlider.bottomAnchor, constant: 4).isActive = true
        
        wantedBloodLevelSelectedValue.translatesAutoresizingMaskIntoConstraints = false
        wantedBloodLevelSelectedValue.font = .systemFont(ofSize: 16)
        wantedBloodLevelSelectedValue.text = ""
        wantedBloodLevelSelectedValue.sizeToFit()
        wantedBloodLevelSelectedValue.isScrollEnabled = false
        wantedBloodLevelSelectedValue.bottomAnchor.constraint(equalTo: wantedBloodLevelSlider.topAnchor, constant: 4).isActive = true
        wantedBloodLevelSelectedValue.rightAnchor.constraint(equalTo: wantedBloodLevelSlider.rightAnchor).isActive = true
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
        hoursDrinkingImageView.topAnchor.constraint(equalTo: wantedBloodLevelText.bottomAnchor, constant: 16).isActive = true
        
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
