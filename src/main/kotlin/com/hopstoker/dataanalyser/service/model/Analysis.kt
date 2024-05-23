package com.hopstoker.dataanalyser.service.model

data class Analysis(
    val min: Double,
    val max: Double,
    val first: Double,
    val last: Double,
    val avg: Double,
    val `var`: Double?
) {
    companion object {
        fun identity(data: Double) = Analysis(
            data, data, data, data, data, null
        )
    }
}
