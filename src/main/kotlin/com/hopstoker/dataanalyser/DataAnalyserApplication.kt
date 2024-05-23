package com.hopstoker.dataanalyser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary


@SpringBootApplication
class DataAnalyserApplication

fun main(args: Array<String>) {
    runApplication<DataAnalyserApplication>(*args)
}

@Bean
@Primary
fun objectMapper(): ObjectMapper {
    return ObjectMapper()
        .registerModule(KotlinModule.Builder().build())
}
