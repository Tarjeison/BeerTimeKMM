//
//  MyDrinkItemCell.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 01/11/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit

class MyDrinkItemCell: UITableViewCell {
    
    var drinkIcon = UIImageView()
    var drinkName = UITextView()
    var drinkDescription = UITextView()
    
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupViews()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupViews()
    }
    
    func setupViews() {
        drinkIcon.translatesAutoresizingMaskIntoConstraints = false
        drinkName.translatesAutoresizingMaskIntoConstraints = false
        drinkDescription.translatesAutoresizingMaskIntoConstraints = false
        
        addSubview(drinkIcon)
        addSubview(drinkName)
        addSubview(drinkDescription)
        
        drinkIcon.leftAnchor.constraint(equalTo: leftAnchor, constant: 32).isActive = true
        drinkIcon.centerYAnchor.constraint(equalTo: centerYAnchor).isActive = true
        drinkIcon.heightAnchor.constraint(equalToConstant: 32).isActive = true
        drinkIcon.widthAnchor.constraint(equalToConstant: 32).isActive = true
        
        drinkName.topAnchor.constraint(greaterThanOrEqualTo: topAnchor).isActive = true
        drinkName.font = .boldSystemFont(ofSize: 12)
        drinkName.sizeToFit()
        drinkName.isScrollEnabled = false
        drinkName.backgroundColor = .clear
        drinkName.leftAnchor.constraint(equalTo: drinkIcon.rightAnchor, constant: 8).isActive = true
        
        drinkDescription.topAnchor.constraint(equalTo: drinkName.bottomAnchor).isActive = true
        drinkDescription.font = .systemFont(ofSize: 10)
        drinkDescription.bottomAnchor.constraint(lessThanOrEqualTo: bottomAnchor).isActive = true
        drinkDescription.isScrollEnabled = false
        drinkDescription.backgroundColor = .clear
        drinkDescription.leftAnchor.constraint(equalTo: drinkName.leftAnchor).isActive = true
    }
}
