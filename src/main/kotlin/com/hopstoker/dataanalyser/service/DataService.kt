package com.hopstoker.dataanalyser.service

import com.hopstoker.dataanalyser.service.model.Analysis
import org.springframework.stereotype.Service

@Service
class DataService(
    val series: MutableMap<String, MutableList<Double>> = mutableMapOf(),
    val analyses: MutableMap<String, MutableMap<Int, Analysis>> = mutableMapOf(),
) {
    fun getSeries(symbol: String): MutableList<Double> {
        return series.compute(symbol) { _, series ->
            series ?: mutableListOf()
        } ?: throw RuntimeException("No series found for symbol: $symbol")
    }

    fun getAnalyses(symbol: String): MutableMap<Int, Analysis> {
        return analyses.compute(symbol) { _, symbolAnalyses ->
            symbolAnalyses ?: mutableMapOf()
        } ?: throw RuntimeException("No analyses found for symbol: $symbol")
    }
}
