//
//  AddDrinkViewController.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 05/01/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import UIKit


class AddDrinkViewController: UIViewController {
    
    private let nameFieldTag = 1
    private let percentageFieldTag = 2
    private let volumeFieldTag = 3
    
    private var selectedImageTag: KotlinInt? = nil
    
    lazy var viewModel: NativeAddDrinkViewModel = {
        NativeAddDrinkViewModel(
            onNameError: { [weak self] errorText in
                if let vc = self {
                    vc.nameFieldErrorLabel.text = errorText
                }
            },
            onPercentageError: { [weak self] errorText in
                if let vc = self {
                    vc.percentageFieldErrorLabel.text = errorText
                }
            },
            onVolumeError: { [weak self] errorText in
                if let vc = self {
                    vc.volumeFieldErrorLabel.text = errorText
                }
            },
            onSuccess: { [weak self] in
                if let vc = self {
                    vc.navigationController?.popViewController(animated: false)
                }
            })

    }()
    
    lazy var topImage: UIImageView = {
        let image = (UIImage(named: "scientific")?.scalePreservingAspectRatio(
            targetSize: CGSize(width: 64, height: 64))
        )!
        let imageView = UIImageView(image: image)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    lazy var nameTextField: UITextField = {
        let textField = UITextField()
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.keyboardType = .default
        textField.delegate = self
        textField.setBottomBorder()
        let placeholderLabel = UILabel()
        placeholderLabel.text = "What would you like to name your drink?"
        placeholderLabel.font = UIFont.italicSystemFont(ofSize: (textField.font?.pointSize)!)
        placeholderLabel.sizeToFit()
        textField.addSubview(placeholderLabel)
        placeholderLabel.frame.origin = CGPoint(x: 0, y: (textField.font?.pointSize)! / 2)
        placeholderLabel.textColor = UIColor.lightGray
        placeholderLabel.isHidden = !(textField.text?.isEmpty ?? false)
        return textField
    }()
    
    lazy var nameFieldErrorLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 12)
        label.textColor = .systemRed
        label.numberOfLines = 0
        label.translatesAutoresizingMaskIntoConstraints = false
        label.sizeToFit()
        return label
    }()
    
    lazy var percentageFieldErrorLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 12)
        label.textColor = .systemRed
        label.numberOfLines = 0
        label.translatesAutoresizingMaskIntoConstraints = false
        label.sizeToFit()
        return label
    }()
    
    lazy var volumeFieldErrorLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 12)
        label.textColor = .systemRed
        label.numberOfLines = 0
        label.translatesAutoresizingMaskIntoConstraints = false
        label.sizeToFit()
        return label
    }()
    
    lazy var percentageTextInput: UITextField = {
        let textField = UITextField()
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.keyboardType = .decimalPad
        textField.delegate = self
        textField.setBottomBorder()
        let placeholderLabel = UILabel()
        placeholderLabel.text = "How strong is your drink in alcohol %?"
        placeholderLabel.font = UIFont.italicSystemFont(ofSize: (textField.font?.pointSize)!)
        placeholderLabel.sizeToFit()
        textField.addSubview(placeholderLabel)
        placeholderLabel.frame.origin = CGPoint(x: 0, y: (textField.font?.pointSize)! / 2)
        placeholderLabel.textColor = UIColor.lightGray
        placeholderLabel.isHidden = !(textField.text?.isEmpty ?? false)
        return textField
    }()
    
    lazy var volumeTextInput: UITextField = {
        let textField = UITextField()
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.keyboardType = .decimalPad
        textField.delegate = self
        textField.setBottomBorder()
        let placeholderLabel = UILabel()
        placeholderLabel.text = "How large is your drink in liters?"
        placeholderLabel.font = UIFont.italicSystemFont(ofSize: (textField.font?.pointSize)!)
        placeholderLabel.sizeToFit()
        textField.addSubview(placeholderLabel)
        placeholderLabel.frame.origin = CGPoint(x: 0, y: (textField.font?.pointSize)! / 2)
        placeholderLabel.textColor = UIColor.lightGray
        placeholderLabel.isHidden = !(textField.text?.isEmpty ?? false)
        return textField
    }()
    
    lazy var stackView: UIStackView = {
        let stack = UIStackView()
        stack.axis = .horizontal
        stack.spacing = 32
        stack.alignment = .center
        stack.distribution = .equalCentering
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    lazy var saveButton: UIButton = {
        let button = UIButton(frame: CGRect(x: 0, y: 0, width: 400, height: 94))
         button.translatesAutoresizingMaskIntoConstraints = false
         button.setTitle("Save drink", for: .normal)
         button.layer.cornerRadius = 15
         button.contentEdgeInsets = UIEdgeInsets.init(top: 10,left: 10,bottom: 10,right: 10)
         button.backgroundColor = UIColor(named: "Green")
      
         return button
    }()
    
    override func viewWillAppear(_ animated: Bool) {
        viewModel.observeDrinkResultsFlow()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        viewModel.onDestroy()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        view.addSubview(topImage)
        view.addSubview(nameTextField)
        view.addSubview(percentageTextInput)
        view.addSubview(volumeTextInput)
        view.addSubview(saveButton)
        view.addSubview(nameFieldErrorLabel)
        view.addSubview(volumeFieldErrorLabel)
        view.addSubview(percentageFieldErrorLabel)
        view.addSubview(stackView)
        
        topImage.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 32).isActive = true
        topImage.centerXAnchor.constraint(equalTo: view.safeAreaLayoutGuide.centerXAnchor).isActive = true
        
        nameTextField.topAnchor.constraint(equalTo: topImage.bottomAnchor, constant: 32).isActive = true
        nameTextField.leftAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leftAnchor, constant: 16).isActive = true
        nameTextField.rightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.rightAnchor, constant: -16).isActive = true
        nameTextField.heightAnchor.constraint(equalToConstant: 32).isActive = true
        
        nameFieldErrorLabel.topAnchor.constraint(equalTo: nameTextField.bottomAnchor, constant: 4).isActive = true
        nameFieldErrorLabel.leftAnchor.constraint(equalTo: nameTextField.leftAnchor).isActive = true
        nameFieldErrorLabel.rightAnchor.constraint(equalTo: nameTextField.rightAnchor).isActive = true
        
        percentageTextInput.topAnchor.constraint(equalTo: nameFieldErrorLabel.bottomAnchor, constant: 32).isActive = true
        percentageTextInput.leftAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leftAnchor, constant: 16).isActive = true
        percentageTextInput.rightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.rightAnchor, constant: -16).isActive = true
        percentageTextInput.heightAnchor.constraint(equalToConstant: 32).isActive = true
        
        percentageFieldErrorLabel.topAnchor.constraint(equalTo: percentageTextInput.bottomAnchor, constant: 4).isActive = true
        percentageFieldErrorLabel.leftAnchor.constraint(equalTo: percentageTextInput.leftAnchor).isActive = true
        percentageFieldErrorLabel.rightAnchor.constraint(equalTo: percentageTextInput.rightAnchor).isActive = true
        
        volumeTextInput.topAnchor.constraint(equalTo: percentageFieldErrorLabel.bottomAnchor, constant: 32).isActive = true
        volumeTextInput.leftAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leftAnchor, constant: 16).isActive = true
        volumeTextInput.rightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.rightAnchor, constant: -16).isActive = true
        volumeTextInput.heightAnchor.constraint(equalToConstant: 32).isActive = true
        
        volumeFieldErrorLabel.topAnchor.constraint(equalTo: volumeTextInput.bottomAnchor, constant: 4).isActive = true
        volumeFieldErrorLabel.leftAnchor.constraint(equalTo: volumeTextInput.leftAnchor).isActive = true
        volumeFieldErrorLabel.rightAnchor.constraint(equalTo: volumeTextInput.rightAnchor).isActive = true
        
        saveButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32).isActive = true
        saveButton.centerXAnchor.constraint(equalTo: view.safeAreaLayoutGuide.centerXAnchor).isActive = true
        saveButton.addTarget(self, action: #selector(onSaveDrinkPressed), for: .touchUpInside)
        
        stackView.topAnchor.constraint(equalTo: volumeFieldErrorLabel.bottomAnchor, constant: 32).isActive = true
        stackView.leftAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leftAnchor, constant: 32).isActive = true
        stackView.rightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.rightAnchor, constant: -32).isActive = true
        stackView.heightAnchor.constraint(equalToConstant: 64).isActive = true
        
        viewModel.getDrinkIcons().forEach { item in
            let button = UIButton()
            button.tag = Int(item.tag)
            let image = UIImage(named: item.iconString)?.scalePreservingAspectRatio(targetSize: CGSize(width: 32, height: 32))
            button.setImage(image, for: .normal)
            button.contentEdgeInsets = .init(top: 10, left: 10, bottom: 10, right: 10)
            button.addTarget(self, action: #selector(iconButtonTapped(button:)), for: .touchUpInside)
            stackView.addArrangedSubview(button)
        }
        view.layoutIfNeeded()
        
        hideKeyboardWhenTappedAround()
    }
    
    @objc func iconButtonTapped(button: UIButton) {
        stackView.subviews.forEach { view in
            view.backgroundColor = .white
        }
        button.backgroundColor = .lightGray
        selectedImageTag = KotlinInt(integerLiteral: button.tag)
    }
    
    @objc func onSaveDrinkPressed() {
        nameFieldErrorLabel.text = nil
        volumeFieldErrorLabel.text = nil
        percentageFieldErrorLabel.text = nil
        viewModel.addDrink(drinkName: nameTextField.text, drinkPercentage: percentageTextInput.text, drinkVolume: volumeTextInput.text, drinkIconTag: selectedImageTag)
    }
}

extension AddDrinkViewController: UITextFieldDelegate {
    
    func textFieldDidChangeSelection(_ textField: UITextField) {
        let label: UILabel? = textField.subviews.first as? UILabel ?? nil
        label?.isHidden = !(textField.text?.isEmpty ?? true)
    }
}
