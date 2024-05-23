package com.hopstoker.dataanalyser.service

import com.hopstoker.dataanalyser.service.model.Analysis
import org.springframework.stereotype.Service
import kotlin.math.pow

@Service
class DataAnalyserService(val dataService: DataService) {

    fun addData(symbol: String, data: List<Double>) {
        val analyses = dataService.getAnalyses(symbol)
        val series = dataService.getSeries(symbol)
        for (dataPoint in data) {
            addData(analyses, series, symbol, dataPoint)
        }
    }

    fun addData(
        analyses: MutableMap<Int, Analysis>,
        series: MutableList<Double>,
        symbol: String,
        data: Double
    ) {
        for (exponent in 1..7) {
            analyses.compute(exponent) { exp, analysis ->
                analysisRemapping(series, exp, analysis, data)
            }
        }
        series.addLast(data)
    }

    fun analysisRemapping(
        series: MutableList<Double>,
        exponent: Int,
        analysis: Analysis?,
        newData: Double
    ): Analysis {
        return if (series.size < getRange(exponent) || analysis == null) {
            analysePartialSeries(series, analysis, newData)
        } else {
            analyseFullSeries(series, exponent, analysis, newData)
        }
    }

    fun analysePartialSeries(
        series: MutableList<Double>,
        analysis: Analysis?,
        newData: Double
    ): Analysis {
        return if (analysis != null) {
            val sampleRange = series.size

            val oldData = analysis.first
            val newMinMax = getMinMax(analysis, series, sampleRange, oldData, newData)
            val newMean = Stats.getMeanAddedNumber(sampleRange, analysis.avg, newData)
            val newVariance = if (analysis.`var` == null) {
                Stats.getPopulationVariance(series, newMean)
            } else {
                Stats.getPopulationVariance(sampleRange, analysis.`var`, analysis.avg, newMean, newData)
            }

            return Analysis(
                newMinMax.first,
                newMinMax.second,
                analysis.first,
                newData,
                newMean,
                newVariance
            )
        } else {
            Analysis.identity(newData)
        }
    }

    fun analyseFullSeries(
        series: MutableList<Double>,
        exponent: Int,
        analysis: Analysis,
        newData: Double
    ): Analysis {
        if (analysis.`var` == null) {
            throw IllegalStateException("Cannot analyse full series with unknown input variance")
        }

        val sampleRange = getRange(exponent)

        val oldData = analysis.first
        val newFirst = series[series.size - sampleRange + 1]
        val newMinMax = getMinMax(analysis, series, sampleRange - 1, oldData, newData)
        val newMean = Stats.getMeanSwappedNumber(sampleRange, oldData, newData, analysis.avg)
        val newVariance = Stats.getSampleVariance(sampleRange, analysis.`var`, analysis.avg, newMean, oldData, newData)

        return Analysis(
            newMinMax.first,
            newMinMax.second,
            newFirst,
            newData,
            newMean,
            newVariance
        )
    }

    companion object {
        fun getMinMax(
            analysis: Analysis,
            series: List<Double>,
            range: Int,
            oldValue: Double,
            newValue: Double
        ): Pair<Double, Double> {

            val min = if (newValue <= analysis.min) {
                newValue
            } else if (analysis.min < oldValue) {
                analysis.min
            } else {
                null
            }

            val max = if (newValue >= analysis.max) {
                newValue
            } else if (analysis.max > oldValue) {
                analysis.max
            } else {
                null
            }

            return if (min == null || max == null) {
                val minMax = getMinMax(series, range)
                val newMin = min ?: minMax.first
                val newMax = max ?: minMax.second
                Pair(newMin, newMax)
            } else {
                Pair(min, max)
            }
        }

        fun getMinMax(series: List<Double>, range: Int): Pair<Double, Double> {
            var min = Double.MAX_VALUE
            var max = Double.MIN_VALUE
            series.takeLast(range).forEach {
                if (it < min) {
                    min = it
                }
                if (it > max) {
                    max = it
                }
            }
            return Pair(min, max)
        }

        fun getRange(exponent: Int): Int {
            return 10.0.pow(exponent).toInt()
        }
    }
}
