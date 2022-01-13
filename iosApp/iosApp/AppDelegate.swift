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
        
        let tabController = UITabBarController()
        tabController.view.backgroundColor = .white
        tabController.tabBar.tintColor = .black
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
        profileViewNavController.navigationBar.tintColor = .darkGray
        profileViewNavController.navigationBar.standardAppearance = appearance
        profileViewNavController.navigationBar.scrollEdgeAppearance =
        profileViewNavController.navigationBar.standardAppearance
        
        startDrinkingViewNavController.title = "Start drinking"
        startDrinkingViewNavController.tabBarItem.image = UIImage(named: "beer_tab_icon")?.scalePreservingAspectRatio(targetSize: CGSize(width: 22, height: 22))
        countDownViewNavController.title = "Current status"
        countDownViewNavController.tabBarItem.image = UIImage(named: "timer_tab_icon")?.scalePreservingAspectRatio(targetSize: CGSize(width: 22, height: 22))
        profileViewNavController.title = "Me"
        profileViewNavController.tabBarItem.image = UIImage(named: "profile_tab_icon")?.scalePreservingAspectRatio(targetSize: CGSize(width: 22, height: 22))
        tabController.setViewControllers([startDrinkingViewNavController, countDownViewNavController, profileViewNavController], animated: false)
        
        
        self.window = UIWindow(frame: UIScreen.main.bounds)
        self.window?.rootViewController = tabController
        self.window?.makeKeyAndVisible()
        
        // UNUserNotificationCenter.current().delegate = self
        
        return true
    }
}

