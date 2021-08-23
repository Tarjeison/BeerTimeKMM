//
//  WantedBloodLevelView.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 23/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit

class WantedBloodLevelView: UIView {
    let imageSize: CGFloat = 62
    let wantedBloodLevelSliderTag = 1
    
    
    var onSliderChanged: ((Int, Float) -> Void)?
    
    var wantedBloodLevelImageView: UIImageView!
    var wantedBloodLevelSlider: UISlider!
    var wantedBloodLevelText: UITextView!
    var wantedBloodLevelSelectedValue: UITextView!
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        initSubviews()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        initSubviews()
    }
    
    func initSubviews() {
        wantedBloodLevelImageView = UIImageView()
        wantedBloodLevelSlider = UISlider()
        wantedBloodLevelText = UITextView()
        wantedBloodLevelSelectedValue = UITextView()
        
        addSubview(wantedBloodLevelSlider)
        addSubview(wantedBloodLevelText)
        addSubview(wantedBloodLevelImageView)
        addSubview(wantedBloodLevelSelectedValue)
        
        arrangeViews()
    }
    
    func updateText(text: String) {
        wantedBloodLevelSelectedValue.text = text 
    }
    
    func arrangeViews() {
        if let image = UIImage(named: "reading") {
            wantedBloodLevelImageView.image = image
            print("Image found")
            wantedBloodLevelImageView.contentMode = .scaleAspectFit
            wantedBloodLevelImageView.translatesAutoresizingMaskIntoConstraints = false
        }
        
        wantedBloodLevelImageView.widthAnchor.constraint(equalToConstant: imageSize).isActive = true
        wantedBloodLevelImageView.centerXAnchor.constraint(equalTo: centerXAnchor).isActive = true
        wantedBloodLevelImageView.heightAnchor.constraint(equalToConstant: imageSize).isActive = true
        wantedBloodLevelImageView.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 10).isActive = true
        
        wantedBloodLevelSlider.translatesAutoresizingMaskIntoConstraints = false
        wantedBloodLevelSlider.maximumValue = 15
        wantedBloodLevelSlider.minimumValue = 0
        wantedBloodLevelSlider.thumbTintColor = UIColor(named: "Green")
        wantedBloodLevelSlider.topAnchor.constraint(equalTo: wantedBloodLevelImageView.bottomAnchor, constant: 16).isActive = true
        wantedBloodLevelSlider.widthAnchor.constraint(equalTo: safeAreaLayoutGuide.widthAnchor, multiplier: 0.9).isActive = true
        wantedBloodLevelSlider.centerXAnchor.constraint(equalTo: centerXAnchor).isActive = true
        wantedBloodLevelSlider.tag = wantedBloodLevelSliderTag
        wantedBloodLevelSlider.addTarget(self, action: #selector(sliderValueChanged), for: UIControl.Event.valueChanged)
        
        wantedBloodLevelText.translatesAutoresizingMaskIntoConstraints = false
        wantedBloodLevelText.font = .systemFont(ofSize: 16)
        wantedBloodLevelText.text = "Wanted blood level"
        wantedBloodLevelText.sizeToFit()
        wantedBloodLevelText.bottomAnchor.constraint(equalTo: bottomAnchor).isActive = true 
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
    
    @objc func sliderValueChanged(sender: UISlider){
        self.onSliderChanged?(wantedBloodLevelSliderTag, wantedBloodLevelSlider.value)
    }
    
}
