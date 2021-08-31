//
//  HoursDrinkingView.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 24/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit


class HoursDrinkingView: UIView {
    
    
    var hoursDrinkingImageView: UIImageView!
    var hoursDrinkingSlider: UISlider!
    var hoursDrinkingText: UITextView!
    var hoursDrinkingSelectedValue: UITextView!
    var peakHourSlider: UISlider!
    var peakHourText: UITextView!
    var peakHourDisplayText: UITextView!
    
    var onSliderChanged: OnSliderChanged?
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        initViews()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        initViews()
    }
    
    func initViews() {
        translatesAutoresizingMaskIntoConstraints = false
        backgroundColor = .white
        hoursDrinkingImageView = UIImageView()
        hoursDrinkingSlider = UISlider()
        hoursDrinkingText = UITextView()
        hoursDrinkingSelectedValue = UITextView()
        peakHourSlider = UISlider()
        peakHourText = UITextView()
        peakHourDisplayText = UITextView()
        
        addSubview(hoursDrinkingImageView)
        addSubview(hoursDrinkingSlider)
        addSubview(hoursDrinkingText)
        addSubview(hoursDrinkingSelectedValue)
        addSubview(peakHourText)
        addSubview(peakHourSlider)
        addSubview(peakHourDisplayText)
        
        setupViews()
    }
    
    func updateDrinkUntilDisplayValue(text: String) {
        hoursDrinkingSelectedValue.text = text 
    }
    
    func setupViews() {
        if UIImage(named: "drunk") != nil {
            hoursDrinkingImageView.image = UIImage(named: "reading")
            print("Image found")
            hoursDrinkingImageView.contentMode = .scaleAspectFit
            hoursDrinkingImageView.translatesAutoresizingMaskIntoConstraints = false
        }
        
        hoursDrinkingImageView.widthAnchor.constraint(equalToConstant: _imageSize).isActive = true
        hoursDrinkingImageView.centerXAnchor.constraint(equalTo: centerXAnchor).isActive = true
        hoursDrinkingImageView.heightAnchor.constraint(equalToConstant: _imageSize).isActive = true
        hoursDrinkingImageView.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 16).isActive = true
        
        hoursDrinkingSlider.translatesAutoresizingMaskIntoConstraints = false
        hoursDrinkingSlider.maximumValue = 720
        hoursDrinkingSlider.minimumValue = 0
        hoursDrinkingSlider.thumbTintColor = UIColor(named: "Green")
        hoursDrinkingSlider.topAnchor.constraint(equalTo: hoursDrinkingImageView.bottomAnchor, constant: 16).isActive = true
        hoursDrinkingSlider.widthAnchor.constraint(equalTo: widthAnchor, multiplier: 0.9).isActive = true
        hoursDrinkingSlider.centerXAnchor.constraint(equalTo: centerXAnchor).isActive = true
        hoursDrinkingSlider.tag = _hoursDrinkingSliderTag
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
        
        peakHourSlider.translatesAutoresizingMaskIntoConstraints = false
        peakHourSlider.maximumValue = 720
        peakHourSlider.minimumValue = 0
        peakHourSlider.thumbTintColor = UIColor(named: "Green")
        peakHourSlider.topAnchor.constraint(equalTo: hoursDrinkingText.bottomAnchor, constant: 32).isActive = true
        peakHourSlider.widthAnchor.constraint(equalTo: widthAnchor, multiplier: 0.9).isActive = true
        peakHourSlider.centerXAnchor.constraint(equalTo: centerXAnchor).isActive = true
        peakHourSlider.tag = _peakHourSliderTag
        peakHourSlider.addTarget(self, action: #selector(sliderValueChanged), for: UIControl.Event.valueChanged)
        
        peakHourDisplayText.translatesAutoresizingMaskIntoConstraints = false
        peakHourDisplayText.font = .systemFont(ofSize: 16)
        peakHourDisplayText.text = ""
        peakHourDisplayText.sizeToFit()
        peakHourDisplayText.isScrollEnabled = false
        peakHourDisplayText.bottomAnchor.constraint(equalTo: peakHourSlider.topAnchor, constant: 4).isActive = true
        peakHourDisplayText.rightAnchor.constraint(equalTo: peakHourSlider.rightAnchor).isActive = true
        
        peakHourText.translatesAutoresizingMaskIntoConstraints = false
        peakHourText.font = .systemFont(ofSize: 16)
        peakHourText.text = "When do you want to reach your BAC target?"
        peakHourText.sizeToFit()
        peakHourText.isScrollEnabled = false
        peakHourText.bottomAnchor.constraint(equalTo: bottomAnchor).isActive = true
        peakHourText.leftAnchor.constraint(equalTo: peakHourSlider.leftAnchor).isActive = true
        peakHourText.topAnchor.constraint(equalTo: peakHourSlider.bottomAnchor, constant: 4).isActive = true
    }
    
    @objc func sliderValueChanged(sender: UISlider){
        self.onSliderChanged?.sliderChanged(sender.tag, sender.value)
    }
}
