package com.hopstoker.dataanalyser.controller

import com.hopstoker.dataanalyser.controller.model.GetAnalysisResponse
import com.hopstoker.dataanalyser.controller.model.PostDataBulkRequest
import com.hopstoker.dataanalyser.controller.model.PostDataRequest
import com.hopstoker.dataanalyser.controller.model.PostDataResponse
import com.hopstoker.dataanalyser.service.DataAnalyserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
class Controller(val analyserService: DataAnalyserService) {

    @PostMapping("/add", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addOneData(@RequestBody request: PostDataRequest): PostDataResponse {
        analyserService.addData(request.symbol, listOf(request.value))
        return PostDataResponse(request.symbol)
    }

    @PostMapping("/add_batch", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addData(@RequestBody request: PostDataBulkRequest): PostDataResponse {
        analyserService.addData(request.symbol, request.values)
        return PostDataResponse(request.symbol)
    }

    @GetMapping("/stats", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAnalysis(@RequestParam symbol: String, @RequestParam k: Int): GetAnalysisResponse {
        val analysis = analyserService.dataService.analyses[symbol]?.get(k)
        return analysis?.let {
            GetAnalysisResponse(
                it.min, it.max, it.last, it.avg, it.`var`
            )
        } ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "No data found for symbol ${symbol}"
        )
    }
}
