//
//  ChartHelper.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 04/09/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import Charts

func createLimitLine(drawValue: Double) -> ChartLimitLine {
    let limitLine = ChartLimitLine(limit: drawValue)
    limitLine.lineWidth = 1
    limitLine.lineDashLengths = [5]
    limitLine.labelPosition = .topRight
    limitLine.valueFont = .systemFont(ofSize: 10)
    return limitLine
}
