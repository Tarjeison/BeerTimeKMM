package com.pd.beertimer.feature.countdown.charts

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.pd.beertimer.util.toHourMinuteString
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ChartHelper {
    fun setLineDataSetAttributes(lineDataSet: LineDataSet) {
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.cubicIntensity = 0.15f
        lineDataSet.setDrawFilled(true)
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 1f
//                set1.circleHoleColor = Color.BLACK
//                set1.circleRadius = 1.5f
//                set1.setCircleColor(Color.WHITE)
//                set1.highLightColor = Color.rgb(244, 117, 117)
        lineDataSet.color = Color.BLACK
        lineDataSet.fillColor = Color.WHITE
        lineDataSet.fillAlpha = 100
        lineDataSet.setDrawHorizontalHighlightIndicator(false)


        // draw selection line as dashed
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f)


        // customize legend entry
        lineDataSet.setDrawVerticalHighlightIndicator(false)

    }

    fun createLimitLine(drawValue: Float): LimitLine {
        val limitLine = LimitLine(drawValue)
        limitLine.lineWidth = 1f
        limitLine.enableDashedLine(5f, 5f, 0f)
        limitLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        limitLine.textSize = 10f
        return limitLine
    }

    fun createAxisLabelFormatterFromLocalDateTimeList(list: List<Instant>, context: Context): IndexAxisValueFormatter {
        val labels = list.map { instant ->

            instant.toLocalDateTime(TimeZone.currentSystemDefault()).toHourMinuteString(context)
        }
        return IndexAxisValueFormatter(labels)
    }
}
