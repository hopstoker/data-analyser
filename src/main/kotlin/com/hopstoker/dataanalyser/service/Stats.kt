package com.hopstoker.dataanalyser.service

object Stats {

    fun getSampleVariance(
        sampleSize: Int,
        oldVariance: Double,
        oldMean: Double,
        newMean: Double,
        removedValue: Double,
        newValue: Double
    ) =
        ((sampleSize - 1) * oldVariance + (newValue - oldMean) * (newValue - newMean) - (removedValue - oldMean) * (removedValue - newMean)) / (sampleSize - 1)

    fun getPopulationVariance(
        sampleSize: Int,
        oldVariance: Double,
        oldMean: Double,
        newMean: Double,
        newValue: Double
    ): Double {
        return (sampleSize.toDouble() * oldVariance + (newValue - oldMean) * (newValue - newMean)) / (sampleSize.toDouble() + 1)
    }

    fun getPopulationVariance(
        series: List<Double>,
        mean: Double
    ) = series.sumOf { (it - mean) * (it - mean) } / series.size

    fun getMeanSwappedNumber(
        sampleSize: Int,
        oldValue: Double,
        newValue: Double,
        oldMean: Double
    ) = oldMean - (oldValue - newValue) / sampleSize

    fun getMeanAddedNumber(
        oldSampleSize: Int,
        oldMean: Double,
        newValue: Double
    ) = (oldMean * oldSampleSize + newValue) / (oldSampleSize + 1)
}
