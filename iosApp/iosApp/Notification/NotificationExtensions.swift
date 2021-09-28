//
//  NotificationExtensions.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 27/09/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UserNotifications


extension AppDelegate: UNUserNotificationCenterDelegate {
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void)
    {
        let id = response.notification.request.identifier
        print("Received notification with ID = \(id)")
        
        completionHandler()
    }
    
}
