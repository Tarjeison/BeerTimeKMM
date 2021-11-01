//
//  KeyboardViewControllerExtension.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 01/11/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit

extension UIViewController {
    func hideKeyboardWhenTappedAround() {
        let tap = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
}
