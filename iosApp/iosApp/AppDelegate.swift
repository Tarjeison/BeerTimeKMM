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
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = UIColor(named: "Orange")
        startDrinkingViewNavController.navigationBar.barTintColor = UIColor(named: "Orange")
        startDrinkingViewNavController.navigationBar.standardAppearance = appearance
        startDrinkingViewNavController.navigationBar.scrollEdgeAppearance = startDrinkingViewNavController.navigationBar.standardAppearance
        
        countDownViewNavController.navigationBar.barTintColor = UIColor(named: "Orange")
        countDownViewNavController.navigationBar.standardAppearance = appearance
        countDownViewNavController.navigationBar.scrollEdgeAppearance = countDownViewNavController.navigationBar.standardAppearance
        
        profileViewNavController.navigationBar.barTintColor = UIColor(named: "Orange")
        profileViewNavController.navigationBar.standardAppearance = appearance
        profileViewNavController.navigationBar.scrollEdgeAppearance =
        profileViewNavController.navigationBar.standardAppearance
        
        startDrinkingViewNavController.title = "Start drinking"
        countDownViewNavController.title = "Current status"
        profileViewNavController.title = "Profile"
        tabController.setViewControllers([startDrinkingViewNavController, countDownViewNavController, profileViewNavController], animated: false)
        
        
        self.window = UIWindow(frame: UIScreen.main.bounds)
        self.window?.rootViewController = tabController
        self.window?.makeKeyAndVisible()
        
        // UNUserNotificationCenter.current().delegate = self
        
        return true
    }
}

