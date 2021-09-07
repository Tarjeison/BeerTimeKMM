//
//  ProfileViewController.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 07/09/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit

class ProfileViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor).isActive = true 
        view.backgroundColor = .gray
    }
}
