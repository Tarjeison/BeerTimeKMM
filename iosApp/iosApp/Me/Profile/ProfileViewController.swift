//
//  ProfileViewController.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 07/09/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit

class ProfileViewController: UIViewController, UITextFieldDelegate {
    
    private let genderMaleTag = 1
    private let genderFemaleTag = 2
    
    private let ozButtonTag = 1
    private let literButtonTag = 2
    
    private let separatorHeight: CGFloat = 1
    
    lazy var profileIcon: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.image = UIImage(named: "ic_superhero_pineapple")?.scalePreservingAspectRatio(targetSize: CGSize(width: 128, height: 128))
        return imageView
    }()
    
    lazy var genderText: UITextView = {
        let textView = UITextView()
        textView.translatesAutoresizingMaskIntoConstraints = false
        textView.isScrollEnabled = false
        textView.isSelectable = false
        textView.sizeToFit()
        textView.text = "Gender"
        textView.font = .systemFont(ofSize: 16)
        return textView
    }()
    
    lazy var weightText: UITextView = {
        let textView = UITextView()
        textView.translatesAutoresizingMaskIntoConstraints = false
        textView.isScrollEnabled = false
        textView.isSelectable = false
        textView.sizeToFit()
        textView.text = "Weigth"
        textView.font = .systemFont(ofSize: 16)
        return textView
    }()
    
    lazy var maleButton: UIButton = {
        let button = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.tag = genderMaleTag
        let image = UIImage(named: "man")
        button.setImage(image, for: .normal)
        return button
    }()
    
    lazy var femaleButton: UIButton = {
        let button = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.tag = genderFemaleTag
        let image = UIImage(named: "woman")
        button.setImage(image, for: .normal)
        return button
    }()
    
    lazy var weigthTextInput: UITextField = {
        let textField = UITextField()
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.keyboardType = .numberPad
        textField.delegate = self
        textField.setBottomBorder()
        return textField
    }()
    
    lazy var weigthIcon: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.image = UIImage(named: "kg")?.scalePreservingAspectRatio(targetSize: CGSize(width: 32, height: 32))
        return imageView
    }()
    
    lazy var ozCheckButton: UIButton = {
        let button = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.tag = ozButtonTag
        let image = UIImage(named: "unchecked")
        button.setImage(image, for: .normal)
        return button
    }()
    
    lazy var literCheckButton: UIButton = {
        let button = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.tag = literButtonTag
        let image = UIImage(named: "unchecked")
        button.setImage(image, for: .normal)
        return button
    }()
    
    lazy var ozDecription: UITextView = {
        let textView = UITextView()
        textView.translatesAutoresizingMaskIntoConstraints = false
        textView.isScrollEnabled = false
        textView.isSelectable = false
        textView.sizeToFit()
        textView.text = "oz"
        textView.font = .systemFont(ofSize: 16)
        return textView
    }()
    
    lazy var literDescription: UITextView = {
        let textView = UITextView()
        textView.translatesAutoresizingMaskIntoConstraints = false
        textView.isScrollEnabled = false
        textView.isSelectable = false
        textView.sizeToFit()
        textView.text = "L"
        textView.font = .systemFont(ofSize: 16)
        return textView
    }()
    
    lazy var metricDescription: UITextView = {
        let textView = UITextView()
        textView.translatesAutoresizingMaskIntoConstraints = false
        textView.isScrollEnabled = false
        textView.isSelectable = false
        textView.sizeToFit()
        textView.text = "Liters or ounces?"
        textView.font = .systemFont(ofSize: 16)
        return textView
    }()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //view.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor).isActive = true
        view.addSubview(profileIcon)
        profileIcon.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16).isActive = true
        profileIcon.centerXAnchor.constraint(equalTo: view.safeAreaLayoutGuide.centerXAnchor).isActive = true
        profileIcon.heightAnchor.constraint(equalToConstant: 128).isActive = true
        profileIcon.widthAnchor.constraint(equalToConstant: 128).isActive = true
        
        let separator1 = commonSeparator()
        view.addSubview(separator1)
        separator1.topAnchor.constraint(equalTo: profileIcon.bottomAnchor, constant: 16).isActive = true
        separator1.heightAnchor.constraint(equalToConstant: separatorHeight).isActive = true
        separator1.leftAnchor.constraint(equalTo: view.leftAnchor).isActive = true
        separator1.widthAnchor.constraint(equalTo: view.safeAreaLayoutGuide.widthAnchor).isActive = true
        
        view.addSubview(genderText)
        view.addSubview(maleButton)
        view.addSubview(femaleButton)
        genderText.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 16).isActive = true
        genderText.centerYAnchor.constraint(equalTo: maleButton.centerYAnchor).isActive = true
        maleButton.topAnchor.constraint(equalTo: separator1.topAnchor, constant: 8).isActive = true
        maleButton.rightAnchor.constraint(equalTo: view.rightAnchor, constant: -16).isActive = true
        maleButton.heightAnchor.constraint(equalToConstant: 32).isActive = true
        maleButton.widthAnchor.constraint(equalToConstant: 32).isActive = true
        maleButton.addTarget(self, action: #selector(onGenderButtonClicked), for: .touchUpInside)
        femaleButton.addTarget(self, action: #selector(onGenderButtonClicked), for: .touchUpInside)
        femaleButton.rightAnchor.constraint(equalTo: maleButton.leftAnchor, constant: -16).isActive = true
        femaleButton.heightAnchor.constraint(equalToConstant: 32).isActive = true
        femaleButton.widthAnchor.constraint(equalToConstant: 32).isActive = true
        femaleButton.centerYAnchor.constraint(equalTo: maleButton.centerYAnchor).isActive = true
        
        let separator2 = commonSeparator()
        view.addSubview(separator2)
        separator2.topAnchor.constraint(equalTo: maleButton.bottomAnchor, constant: 8).isActive = true
        separator2.heightAnchor.constraint(equalToConstant: separatorHeight).isActive = true
        separator2.leftAnchor.constraint(equalTo: view.leftAnchor).isActive = true
        separator2.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        
        view.addSubview(weigthTextInput)
        view.addSubview(weigthIcon)
        view.addSubview(weightText)
        weigthIcon.topAnchor.constraint(equalTo: separator2.bottomAnchor, constant: 8).isActive = true
        weigthIcon.rightAnchor.constraint(equalTo: view.rightAnchor, constant: -16).isActive = true
        weigthIcon.heightAnchor.constraint(equalToConstant: 32).isActive = true
        weigthIcon.widthAnchor.constraint(equalToConstant: 32).isActive = true
        weigthTextInput.rightAnchor.constraint(equalTo: weigthIcon.leftAnchor, constant: -16).isActive = true
        weigthTextInput.heightAnchor.constraint(equalToConstant: 32).isActive = true
        weigthTextInput.widthAnchor.constraint(equalToConstant: 32).isActive = true
        weigthTextInput.centerYAnchor.constraint(equalTo: weigthIcon.centerYAnchor).isActive = true
        weightText.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 16).isActive = true
        weightText.centerYAnchor.constraint(equalTo: weigthTextInput.centerYAnchor).isActive = true
        
        let separator3 = commonSeparator()
        view.addSubview(separator3)
        separator3.topAnchor.constraint(equalTo: weigthIcon.bottomAnchor, constant: 8).isActive = true
        separator3.heightAnchor.constraint(equalToConstant: separatorHeight).isActive = true
        separator3.leftAnchor.constraint(equalTo: view.leftAnchor).isActive = true
        separator3.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        view.addSubview(ozCheckButton)
        view.addSubview(ozDecription)
        view.addSubview(literCheckButton)
        view.addSubview(literDescription)
        view.addSubview(metricDescription)
        ozCheckButton.topAnchor.constraint(equalTo: separator3.bottomAnchor, constant: 8).isActive = true
        ozCheckButton.rightAnchor.constraint(equalTo: view.rightAnchor, constant: -16).isActive = true
        ozCheckButton.heightAnchor.constraint(equalToConstant: 32).isActive = true
        ozCheckButton.addTarget(self, action: #selector(onMetricButtonClicked), for: .touchUpInside)
        ozCheckButton.widthAnchor.constraint(equalToConstant: 32).isActive = true
        ozDecription.rightAnchor.constraint(equalTo: ozCheckButton.leftAnchor, constant: -8).isActive = true
        ozDecription.centerYAnchor.constraint(equalTo: ozCheckButton.centerYAnchor).isActive = true
        literCheckButton.rightAnchor.constraint(equalTo: ozDecription.leftAnchor, constant: -8).isActive = true
        literCheckButton.heightAnchor.constraint(equalToConstant: 32).isActive = true
        literCheckButton.widthAnchor.constraint(equalToConstant: 32).isActive = true
        literCheckButton.addTarget(self, action: #selector(onMetricButtonClicked), for: .touchUpInside)
        literCheckButton.centerYAnchor.constraint(equalTo: ozCheckButton.centerYAnchor).isActive = true
        literDescription.rightAnchor.constraint(equalTo: literCheckButton.leftAnchor, constant: -8).isActive = true
        literDescription.centerYAnchor.constraint(equalTo: ozCheckButton.centerYAnchor).isActive = true
        metricDescription.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 16).isActive = true
        metricDescription.centerYAnchor.constraint(equalTo: ozCheckButton.centerYAnchor).isActive = true
        let separator4 = commonSeparator()
        view.addSubview(separator4)
        separator4.topAnchor.constraint(equalTo: ozCheckButton.bottomAnchor, constant: 8).isActive = true
        separator4.heightAnchor.constraint(equalToConstant: separatorHeight).isActive = true
        separator4.leftAnchor.constraint(equalTo: view.leftAnchor).isActive = true
        separator4.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        
        
    }
    
    @objc func onGenderButtonClicked(button: UIButton) {
        setSelectedGender(tag: button.tag)
    }
    
    @objc func onMetricButtonClicked(button: UIButton) {
        setSelectedMetric(tag: button.tag)
    }
    
    func setSelectedMetric(tag: Int) {
        switch (tag) {
        case ozButtonTag:
            ozCheckButton.isSelected = true
            ozCheckButton.setImage(UIImage(named: "checked"), for: .normal)
            literCheckButton.setImage(UIImage(named: "unchecked"), for: .normal)
            literCheckButton.isSelected = false
        case literButtonTag:
            literCheckButton.isSelected = true
            literCheckButton.setImage(UIImage(named: "checked"), for: .normal)
            ozCheckButton.setImage(UIImage(named: "unchecked"), for: .normal)
            ozCheckButton.isSelected = false
        default:
            break
        }
    }
    
    func setSelectedGender(tag: Int) {
        switch (tag) {
        case genderFemaleTag:
            femaleButton.isSelected = true
            femaleButton.backgroundColor = UIColor(named: "GrayBackground")
            maleButton.backgroundColor = .white
            maleButton.isSelected = false
        case genderMaleTag:
            femaleButton.isSelected = false
            maleButton.isSelected = true
            femaleButton.backgroundColor = .white
            maleButton.backgroundColor = UIColor(named: "GrayBackground")
        default:
            break
        }
    }
    
    private func commonSeparator() -> UIView {
        let separator = UIView()
        separator.backgroundColor = .lightGray
        separator.translatesAutoresizingMaskIntoConstraints = false
        return separator
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        let currentText = textField.text ?? ""
        
        guard let stringRange = Range(range, in: currentText) else { return false }
        let updatedText = currentText.replacingCharacters(in: stringRange, with: string)
        
        return updatedText.count <= 3
    }
}


