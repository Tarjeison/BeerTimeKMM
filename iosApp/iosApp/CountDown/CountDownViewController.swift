//
//  CountDownViewController.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 31/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared
import Charts

class CountDownViewController: UIViewController {
    
    var countDownText: UITextView!
    
    lazy var scrollView: UIScrollView = {
        let scrollView = UIScrollView()
        scrollView.isScrollEnabled = true 
        scrollView.translatesAutoresizingMaskIntoConstraints = false
        return scrollView
    }()
    
    lazy var contentView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    lazy var drinkChart: LineChartView = {
        let chart = LineChartView()
        chart.translatesAutoresizingMaskIntoConstraints = false
        chart.isHidden = true
        return chart
    }()
    
    lazy var drinkStatusIcon: UIImageView = {
        let image = UIImage(named: "drunk")
        let imageView = UIImageView(image: image)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    lazy var untilNextTextView: UITextView = {
        let textView = UITextView()
        textView.translatesAutoresizingMaskIntoConstraints = false
        textView.font = .systemFont(ofSize: 16)
        textView.isScrollEnabled = false
        textView.isExclusiveTouch = false
        textView.text = "Until next drink"
        return textView
    }()
    
    lazy var nDrinksTextView: UITextView = {
        let textView = UITextView()
        textView.translatesAutoresizingMaskIntoConstraints = false
        textView.font = .systemFont(ofSize: 16)
        textView.isScrollEnabled = false
        textView.layer.cornerRadius = 5
        textView.backgroundColor = UIColor(named: "LightBlue")
        textView.isHidden = true
        return textView
    }()
    
    lazy var stopDrinkingButton: UIButton = {
       let button = UIButton(frame: CGRect(x: 0, y: 0, width: 400, height: 94))
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("Stop drinking", for: .normal)
        button.layer.cornerRadius = 15
        button.contentEdgeInsets = UIEdgeInsets.init(top: 10,left: 10,bottom: 10,right: 10)
        button.backgroundColor = UIColor(named: "Green")
        button.addTarget(self, action: #selector(onStopDrinkingClicked), for: .touchUpInside)
        button.isHidden = true
        return button
    }()
    
    lazy var viewModel = NativeCountDownViewModel(
        onCountDownChanged: { [weak self] displayValue in
            if let vc = self {
                vc.countDownText.text = displayValue
            }
        },
        onCurrentlyDrinking: { [weak self] drinkingModel in
            if let vc = self {
                vc.updateCurrentlyDrinkingView(drinkingModel: drinkingModel)
            }
        },
        onNotStartedDrinking: { [weak self] in
            if let vc = self {
                vc.drinkChart.isHidden = true
                vc.nDrinksTextView.isHidden = true
                vc.stopDrinkingButton.isHidden = true
            }
        },
        onCountDownDescriptionChanged: { [weak self] displayValue in
            if let vc = self {
                vc.untilNextTextView.text = displayValue
            }
        },
        onNUnitsConsumedChanged: { [weak self] displayValue in
            if let vc = self {
                vc.nDrinksTextView.text = displayValue
            }
        }
    )
    
    override func viewWillAppear(_ animated: Bool) {
        viewModel.startCountDown()
        viewModel.observeData()
    }
    
    
    override func viewWillDisappear(_ animated: Bool) {
        viewModel.onDestroy()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        countDownText = UITextView()
        view.addSubview(scrollView)
        scrollView.addSubview(contentView)
        contentView.addSubview(countDownText)
        contentView.addSubview(drinkChart)
        contentView.addSubview(drinkStatusIcon)
        contentView.addSubview(untilNextTextView)
        contentView.addSubview(nDrinksTextView)
        contentView.addSubview(stopDrinkingButton)
        
        scrollView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        scrollView.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        scrollView.topAnchor.constraint(equalTo: view.topAnchor).isActive = true
        scrollView.bottomAnchor.constraint(equalTo: view.bottomAnchor).isActive = true
        
        contentView.centerXAnchor.constraint(equalTo: scrollView.centerXAnchor).isActive = true
        contentView.widthAnchor.constraint(equalTo: scrollView.widthAnchor).isActive = true
        contentView.topAnchor.constraint(equalTo: scrollView.topAnchor).isActive = true
        contentView.bottomAnchor.constraint(equalTo: scrollView.bottomAnchor).isActive = true
        
        stopDrinkingButton.topAnchor.constraint(equalTo: drinkChart.bottomAnchor, constant: 32).isActive = true
        stopDrinkingButton.centerXAnchor.constraint(equalTo: contentView.centerXAnchor).isActive = true
        stopDrinkingButton.bottomAnchor.constraint(equalTo: contentView.bottomAnchor).isActive = true
        
        untilNextTextView.leftAnchor.constraint(equalTo: contentView.centerXAnchor, constant: 32).isActive = true
        untilNextTextView.topAnchor.constraint(equalTo: countDownText.bottomAnchor).isActive = true
        
        nDrinksTextView.topAnchor.constraint(equalTo: drinkStatusIcon.bottomAnchor, constant: 60).isActive = true
        nDrinksTextView.centerXAnchor.constraint(equalTo: contentView.centerXAnchor).isActive = true
        nDrinksTextView.leftAnchor.constraint(equalTo: contentView.leftAnchor, constant: 32).isActive = true
        nDrinksTextView.rightAnchor.constraint(equalTo: contentView.rightAnchor, constant: -32).isActive = true
        nDrinksTextView.textAlignment = .center
        
        drinkStatusIcon.topAnchor.constraint(equalTo: countDownText.bottomAnchor).isActive = true
        drinkStatusIcon.leftAnchor.constraint(equalTo: contentView.leftAnchor, constant: 32).isActive = true
        drinkStatusIcon.widthAnchor.constraint(equalToConstant: 128).isActive = true
        drinkStatusIcon.heightAnchor.constraint(equalToConstant: 128).isActive = true
        
        drinkChart.topAnchor.constraint(equalTo: nDrinksTextView.bottomAnchor, constant: 32).isActive = true
        drinkChart.rightAnchor.constraint(equalTo: contentView.rightAnchor, constant:-8).isActive = true
        drinkChart.leftAnchor.constraint(equalTo: contentView.leftAnchor, constant: 8).isActive = true
        
        drinkChart.heightAnchor.constraint(equalToConstant: 200).isActive = true
        
        countDownText.font = .systemFont(ofSize: 64)
        countDownText.translatesAutoresizingMaskIntoConstraints = false
        countDownText.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 32).isActive = true
        countDownText.centerXAnchor.constraint(equalTo: contentView.centerXAnchor).isActive = true
        countDownText.layoutMargins.left = 10
        countDownText.layoutMargins.right = 10
        countDownText.textAlignment = .center
        countDownText.isScrollEnabled = false
        countDownText.text = "11:20"
        
        updateScrollViewHeight()
        
        let notificationCenter = NotificationCenter.default
        notificationCenter.addObserver(self, selector: #selector(appMovedToBackground), name: UIApplication.willResignActiveNotification, object: nil)
        notificationCenter.addObserver(self, selector: #selector(appBecameActive), name: UIApplication.didBecomeActiveNotification, object: nil)
    }
    
    @objc private func appMovedToBackground()  {
        viewModel.onDestroy()

    }

    @objc private func appBecameActive()  {
        viewModel.startCountDown()
        viewModel.observeData()
    }

    
    private func updateCurrentlyDrinkingView(drinkingModel: DrinkStatusModel.Drinking) {
        nDrinksTextView.isHidden = false
        nDrinksTextView.layoutIfNeeded()
        drinkChart.isHidden = false
        drinkChart.layoutIfNeeded()
        stopDrinkingButton.isHidden = false
        stopDrinkingButton.layoutIfNeeded()
        
        let entryList = drinkingModel.graphList.map { element in
            ChartDataEntry(x: Double(element.x), y: Double(element.y), icon: UIImage(named: element.iconName)?.scalePreservingAspectRatio(targetSize: CGSize(width: 10, height: 10)))
        }
        let set = LineChartDataSet(entries: entryList)
        set.mode = .cubicBezier
        set.cubicIntensity = 0.15
        set.drawFilledEnabled = true
        set.lineWidth = 1
        set.colors = [NSUIColor.black]
        set.fillColor = NSUIColor.white
        set.fillAlpha = 1
        set.drawHorizontalHighlightIndicatorEnabled = false
        set.drawVerticalHighlightIndicatorEnabled = false
        set.drawCirclesEnabled = false 
        set.fillColor = NSUIColor(named: "Orange") ?? NSUIColor.orange
        drinkChart.xAxis.valueFormatter = IndexAxisValueFormatter(values: drinkingModel.graphList.map({ entry in
            entry.drinkAt.toDisplayValue()
        }))
        drinkChart.xAxis.granularity = 1
        drinkChart.leftAxis.removeAllLimitLines()
        drinkChart.leftAxis.addLimitLine(createLimitLine(drawValue: Double(drinkingModel.wantedBloodLevel * 10)))
        drinkChart.leftAxis.axisMinimum = 0
        
        drinkChart.leftAxis.spaceMax = 0.2
        drinkChart.leftAxis.axisMaximum = Double(drinkingModel.wantedBloodLevel * 10 + 0.2)
        drinkChart.rightAxis.drawGridLinesEnabled = false
        drinkChart.rightAxis.enabled = false
        drinkChart.rightAxis.drawLabelsEnabled = false
        // drinkChart.description.enabled
        drinkChart.legend.enabled = false
        
        drinkChart.isMultipleTouchEnabled = false
        drinkChart.animate(xAxisDuration: 0.25)
        let data = LineChartData(dataSet: set)
        data.setValueFont(.systemFont(ofSize: 9))
        data.setDrawValues(false)
        
        drinkChart.data = data
        drinkChart.extraRightOffset = 20
        drinkChart.sizeToFit()
    }
    
    func updateScrollViewHeight() {
        scrollView.contentSize = CGSize(width: UIScreen.main.bounds.width, height: contentView.frame.height)
    }
    
    @objc func onStopDrinkingClicked() {
        let alert = UIAlertController(title: "Are you sure?", message: "You will lose your current progress and all alarms will be deleted", preferredStyle: .alert)

        alert.addAction(UIAlertAction(title: "Yes", style: .default, handler: { _ in
            self.viewModel.stopDrinking()
        }))
        alert.addAction(UIAlertAction(title: "No", style: .cancel, handler: nil))
        self.present(alert, animated: true)
    }
}
