//
//  AppDelegate.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 22/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
import shared

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?


    func application(_ application: UIApplication, didFinishLaunchingWithOptions
        launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        startKoin()
        let profileStorage = koin.get(objCClass: ProfileStorage.self) as! ProfileStorage
        profileStorage.saveUserProfile(userProfile: UserProfile(gender: Gender.male, weight: 75))

        let tabController = UITabBarController()
        tabController.view.backgroundColor = .white
        let startDrinkingViewNavController = UINavigationController(rootViewController: StartDrinkingViewController())
        let countDownViewNavController = UINavigationController(rootViewController: CountDownViewController())
        let profileViewNavController = UINavigationController(rootViewController: MePageViewController())
        startDrinkingViewNavController.navigationBar.barTintColor = UIColor(named: "Orange")
        countDownViewNavController.navigationBar.barTintColor = UIColor(named: "Orange")
        profileViewNavController.navigationBar.barTintColor = UIColor(named: "Orange")
        startDrinkingViewNavController.title = "Start drinking"
        countDownViewNavController.title = "Current status"
        profileViewNavController.title = "Profile"
        tabController.setViewControllers([startDrinkingViewNavController, countDownViewNavController, profileViewNavController], animated: false)
        
        
        self.window = UIWindow(frame: UIScreen.main.bounds)
        self.window?.rootViewController = tabController
        self.window?.makeKeyAndVisible()
        
        return true
    }
}

