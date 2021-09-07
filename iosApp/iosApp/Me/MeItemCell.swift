//
//  MeItemCell.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 07/09/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

class MeItemCell : UICollectionViewCell {
    
    let imageView: UIImageView = {
        let iv = UIImageView()
        iv.translatesAutoresizingMaskIntoConstraints = false
        iv.contentMode = .scaleAspectFit
        return iv
    }()
    
    let title: UITextView = {
        let tv = UITextView()
        tv.translatesAutoresizingMaskIntoConstraints = false
        tv.font = .systemFont(ofSize: 11)
        tv.isScrollEnabled = false
        tv.textAlignment = .center
        tv.isSelectable = false
        return tv
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        addSubview(imageView)
        addSubview(title)

        imageView.topAnchor.constraint(greaterThanOrEqualTo: topAnchor).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 32).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 32).isActive = true
        imageView.centerXAnchor.constraint(equalTo: centerXAnchor).isActive = true
        
        title.centerXAnchor.constraint(equalTo: centerXAnchor).isActive = true
        title.topAnchor.constraint(equalTo: imageView.bottomAnchor, constant: 8).isActive = true
        title.leftAnchor.constraint(equalTo: leftAnchor).isActive = true
        title.rightAnchor.constraint(equalTo: rightAnchor).isActive = true
        title.bottomAnchor.constraint(greaterThanOrEqualTo: bottomAnchor, constant: -16).isActive = true
        
    }
    
    func setModel(meItem: MePageItem) {
        title.text = meItem.title
        title.sizeToFit()
        imageView.image = UIImage(named: meItem.iconName)?.scalePreservingAspectRatio(targetSize: CGSize(width: 32, height: 32))
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
