//
//  NotificationScheduler.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 26/09/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import shared
import UserNotifications

let notificationIdentifier = "beertime_local_notification"

class NotificationScheduler: DrinkNotificationScheduler {
    func scheduleNotification(drinkingTimes: [Kotlinx_datetimeInstant]) {
        
        drinkingTimes.forEach { dateTime in
            let secondsUntilNotification = Double(dateTime.epochSeconds) - NSDate().timeIntervalSince1970
            print(secondsUntilNotification)
            let notificationTrigger = UNTimeIntervalNotificationTrigger(timeInterval: secondsUntilNotification, repeats: false)
            
            let notificationContent = UNMutableNotificationContent()
            notificationContent.title = "BeerTime"
            notificationContent.body = "Time for another one!"
            
            let notificationRequest = UNNotificationRequest(identifier: String(dateTime.epochSeconds), content: notificationContent, trigger: notificationTrigger)

            UNUserNotificationCenter.current().add(notificationRequest) { (error) in
                if let error = error {
                    print("Unable to Add Notification Request (\(error), \(error.localizedDescription))")
                }
            }
        }
    }
    
    func cancelAlarm() {
        let center = UNUserNotificationCenter.current()
        center.removeAllPendingNotificationRequests()
    }
}

func requestAuthorization(completionHandler: @escaping (_ success: Bool) -> ()) {
    // Request Authorization
    UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { (success, error) in
        if let error = error {
            print("Request Authorization Failed (\(error), \(error.localizedDescription))")
        }

        completionHandler(success)
    }
}
