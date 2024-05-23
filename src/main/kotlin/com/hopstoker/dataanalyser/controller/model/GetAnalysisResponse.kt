package com.hopstoker.dataanalyser.controller.model

data class GetAnalysisResponse(
    val min: Double,
    val max: Double,
    val last: Double,
    val avg: Double,
    val `var`: Double?
)
