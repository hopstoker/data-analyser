package com.hopstoker.dataanalyser.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.shouldBe

class StatsTest : StringSpec({

    "getSampleVariance returns good variance" {
        val variance = Stats.getSampleVariance(
            5,
            2.5,
            3.0,
            4.2,
            1.0,
            7.0
        )
        variance shouldBeLessThan 3.7
        variance shouldBeGreaterThan 3.6
    }

    "getPopulationVariance form series returns good variance" {
        Stats.getPopulationVariance(
            listOf(1.0, 2.0, 3.0, 4.0, 5.0),
            3.0
        ) shouldBe 2.0
    }

    "getPopulationVariance returns good variance" {
        val variance = Stats.getPopulationVariance(
            5,
            2.0,
            3.0,
            3.5,
            6.0
        )
        variance shouldBeLessThan 2.92
        variance shouldBeGreaterThan 2.91
    }

    "getMeanSwappedNumber returns good mean" {
        Stats.getMeanSwappedNumber(
            5,
            1.0,
            7.0,
            3.0
        ) shouldBe 4.2
    }

    "getMeanAddedNumber returns good mean" {
        Stats.getMeanAddedNumber(
            5,
            3.0,
            6.0
        ) shouldBe 3.5
    }
})
