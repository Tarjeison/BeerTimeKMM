//
//  Toast.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 30/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit

extension UIViewController {
    
    func showToast(message : String) {
        
        let toastLabel = UILabel()
        self.view.addSubview(toastLabel)
        toastLabel.translatesAutoresizingMaskIntoConstraints = false
        toastLabel.heightAnchor.constraint(greaterThanOrEqualToConstant: 50).isActive = true
        toastLabel.widthAnchor.constraint(equalToConstant: 200).isActive = true
        toastLabel.centerXAnchor.constraint(equalTo: self.view.centerXAnchor).isActive = true
        toastLabel.topAnchor.constraint(equalTo: self.view.topAnchor, constant: 100).isActive = true 
        toastLabel.backgroundColor = UIColor.blue
        toastLabel.textColor = UIColor.black
        toastLabel.numberOfLines = 4
        toastLabel.textAlignment = .center;
        toastLabel.font = UIFont.systemFont(ofSize: 10)
        toastLabel.text = message
        toastLabel.lineBreakMode = .byWordWrapping
        toastLabel.lineBreakStrategy = .standard
        toastLabel.alpha = 1.0
        toastLabel.layer.cornerRadius = 10;
        toastLabel.clipsToBounds  =  true
        
        UIView.animate(withDuration: 2, delay: 5, options: .curveEaseOut, animations: {
            toastLabel.alpha = 0.0
        }, completion: {(isCompleted) in
            toastLabel.removeFromSuperview()
        })
    } }
