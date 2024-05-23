package com.hopstoker.dataanalyser.service

import com.hopstoker.dataanalyser.service.model.Analysis
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class DataAnalyserServiceTest : FreeSpec({

    "addData correctly populates series and analyses for incomplete ranges" - {
        listOf(
            listOf(1.0) to Analysis(
                1.0, 1.0, 1.0, 1.0, 1.0, null
            ),
            listOf(1.0, 2.0) to Analysis(
                1.0, 2.0, 1.0, 2.0, 1.5, 0.25
            ),
            listOf(1.0, 2.0, 3.0, 4.0, 5.0) to Analysis(
                1.0, 5.0, 1.0, 5.0, 3.0, 2.0
            )
        ).forEach { (series: List<Double>, expectedAnalysis: Analysis) ->
            "$series, $expectedAnalysis" {
                val dataService = DataService()
                val analyserService = DataAnalyserService(dataService)

                val symbol = "one"

                analyserService.addData(symbol, series)

                dataService.series[symbol] shouldBe series
                dataService.analyses[symbol] shouldBe (1..7).associateWith { expectedAnalysis }
            }
        }
    }

    "addData correctly populates series and analyses for complete ranges" {
        val dataService = DataService()
        val analyserService = DataAnalyserService(dataService)

        val symbol = "one"

        val series = (1..11).map { it.toDouble() }
        val expectedAnalysis10 = Analysis(2.0, 11.0, 2.0, 11.0, 6.5, 8.25)
        val expectedAnalysisOther = Analysis(1.0, 11.0, 1.0, 11.0, 6.0, 10.0)
        analyserService.addData(symbol, series)

        val expectedAnalyses = mutableMapOf(1 to expectedAnalysis10)
        expectedAnalyses.putAll((2..7).associateWith { expectedAnalysisOther })

        dataService.series[symbol] shouldBe series
        dataService.analyses[symbol] shouldBe expectedAnalyses
    }

    "getMinMax" - {

        "min removed; max added" {
            val analysis = Analysis(1.0, 5.0, 1.0, 5.0, 3.0, 2.0)

            val series = listOf(1.0, 2.0, 3.0, 4.0, 5.0)
            DataAnalyserService.getMinMax(analysis, series, 4, 1.0, 6.0) shouldBe Pair(2.0, 6.0)
        }

        "min not removed; max added" {
            val analysis = Analysis(1.0, 5.0, 2.0, 5.0, 3.0, 2.0)

            val series = listOf(2.0, 3.0, 1.0, 4.0, 5.0)
            DataAnalyserService.getMinMax(analysis, series, 4, 2.0, 6.0) shouldBe Pair(1.0, 6.0)
        }


        "min not removed; min new first" {
            val analysis = Analysis(1.0, 5.0, 2.0, 5.0, 3.0, 2.0)

            val series = listOf(2.0, 1.0, 3.0, 4.0, 5.0)
            DataAnalyserService.getMinMax(analysis, series, 4, 2.0, 6.0) shouldBe Pair(1.0, 6.0)
        }

        "max removed" {
            val analysis = Analysis(1.0, 5.0, 5.0, 2.0, 3.0, 2.0)

            val series = listOf(5.0, 1.0, 3.0, 4.0, 2.0)
            DataAnalyserService.getMinMax(analysis, series, 4, 5.0, 1.5) shouldBe Pair(1.0, 4.0)
        }

        "max removed; max added" {
            val analysis = Analysis(1.0, 5.0, 5.0, 2.0, 3.0, 2.0)

            val series = listOf(5.0, 1.0, 3.0, 4.0, 2.0)
            DataAnalyserService.getMinMax(analysis, series, 4, 5.0, 6.0) shouldBe Pair(1.0, 6.0)
        }

        "min removed; min added" {
            val analysis = Analysis(1.0, 5.0, 1.0, 2.0, 3.0, 2.0)

            val series = listOf(1.0, 5.0, 3.0, 4.0, 2.0)
            DataAnalyserService.getMinMax(analysis, series, 4, 1.0, 0.5) shouldBe Pair(0.5, 5.0)
        }

        "min max unchanged" {
            val analysis = Analysis(1.0, 5.0, 3.0, 2.0, 3.0, 2.0)

            val series = listOf(3.0, 1.0, 5.0, 4.0, 2.0)
            DataAnalyserService.getMinMax(analysis, series, 4, 3.0, 1.5) shouldBe Pair(1.0, 5.0)
        }
    }

    "getMinMax returns good values for whole series" - {
        listOf(
            10 to Pair(1.0, 11.0),
            6 to Pair(1.0, 11.0),
            2 to Pair(5.0, 11.0),
            1 to Pair(5.0, 5.0),
        ).forEach { (range: Int, expectedMinMax: Pair<Double, Double>) ->
            "$range, $expectedMinMax" {
                val series = listOf(10.0, 1.0, 3.3, 4.1, 11.0, 5.0)
                DataAnalyserService.getMinMax(series, range) shouldBe expectedMinMax
            }
        }
    }
})
