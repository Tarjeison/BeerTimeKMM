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
    
    lazy var drinkChart: LineChartView = {
        return LineChartView()
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
        view.addSubview(countDownText)
        view.addSubview(drinkChart)
        drinkChart.translatesAutoresizingMaskIntoConstraints = false
        drinkChart.isHidden = false
        drinkChart.topAnchor.constraint(equalTo: countDownText.bottomAnchor).isActive = true
        drinkChart.rightAnchor.constraint(equalTo: view.rightAnchor).isActive = true
        drinkChart.leftAnchor.constraint(equalTo: view.leftAnchor).isActive = true
        drinkChart.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        drinkChart.heightAnchor.constraint(equalToConstant: 200).isActive = true
        
        countDownText.font = .systemFont(ofSize: 64)
        countDownText.translatesAutoresizingMaskIntoConstraints = false
        countDownText.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 32).isActive = true
        countDownText.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        countDownText.sizeToFit()
        countDownText.isScrollEnabled = false
        countDownText.text = "11:20"
        
    }
    
    private func updateCurrentlyDrinkingView(drinkingModel: DrinkStatusModel.Drinking) {
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
        drinkChart.leftAxis.addLimitLine(createLimitLine(drawValue: Double(drinkingModel.wantedBloodLevel * 10)))
        drinkChart.leftAxis.axisMinimum = 0
        drinkChart.leftAxis.axisMaximum = Double(drinkingModel.graphList.map({ entry in entry.y }).max() ?? 1.5 + 0.2)
        drinkChart.rightAxis.drawGridLinesEnabled = false
        drinkChart.rightAxis.drawLabelsEnabled = false
        // drinkChart.description.enabled
        drinkChart.legend.enabled = false
        drinkChart.isMultipleTouchEnabled = false
        drinkChart.animate(xAxisDuration: 0.25)
        let data = LineChartData(dataSet: set)
        data.setValueFont(.systemFont(ofSize: 9))
        data.setDrawValues(false)
        drinkChart.isHidden = false
        drinkChart.data = data
        
    }
}
