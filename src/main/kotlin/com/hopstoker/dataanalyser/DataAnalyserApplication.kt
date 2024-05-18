package com.hopstoker.dataanalyser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DataAnalyserApplication

fun main(args: Array<String>) {
    runApplication<DataAnalyserApplication>(*args)
}
