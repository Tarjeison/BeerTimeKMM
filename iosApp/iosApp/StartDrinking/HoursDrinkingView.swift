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
    
    var onSliderChanged: OnSliderChanged?
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        initSubviews()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        initSubviews()
    }
    
    func initSubviews() {
        hoursDrinkingImageView = UIImageView()
        hoursDrinkingSlider = UISlider()
        hoursDrinkingText = UITextView()
        hoursDrinkingSelectedValue = UITextView()
        
        addSubview(hoursDrinkingImageView)
        addSubview(hoursDrinkingSlider)
        addSubview(hoursDrinkingText)
        addSubview(hoursDrinkingSelectedValue)
        
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
        hoursDrinkingSlider.tag = 2
        hoursDrinkingSlider.addTarget(self, action: #selector(sliderValueChanged), for: UIControl.Event.valueChanged)
        
        hoursDrinkingText.translatesAutoresizingMaskIntoConstraints = false
        hoursDrinkingText.font = .systemFont(ofSize: 16)
        hoursDrinkingText.text = "How long will you be drinking?"
        hoursDrinkingText.sizeToFit()
        hoursDrinkingText.isScrollEnabled = false
        hoursDrinkingText.bottomAnchor.constraint(equalTo: bottomAnchor).isActive = true
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
        self.onSliderChanged?.sliderChanged(sender.tag, sender.value)
    }
}
