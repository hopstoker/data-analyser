package com.hopstoker.dataanalyser

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.springframework.http.HttpStatus
import java.util.*

class DataAnalyserServiceTest : StringSpec({

    val client = OkHttpClient()
    val applicationJson = "application/json".toMediaType()
    val mapper = ObjectMapper()

    "add OK" {
        val symbol = UUID.randomUUID().toString()
        val data = 2.123
        val request = mapOf("symbol" to symbol, "value" to data)
        val expectedResponse = mapOf("symbol" to symbol)

        val addRequestBody = RequestBody.create(applicationJson, mapper.writeValueAsString(request))
        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/add")
                .post(addRequestBody)
                .build()
        ).execute().apply {
            code shouldBe HttpStatus.OK.value()
            body?.string() shouldBe mapper.writeValueAsString(expectedResponse)
        }
    }

    "add BAD REQUEST" {
        val request = mapOf<String, Any>()
        val addRequestBody = RequestBody.create(applicationJson, mapper.writeValueAsString(request))

        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/add")
                .post(addRequestBody)
                .build()
        ).execute().code shouldBe HttpStatus.BAD_REQUEST.value()
    }

    "add_batch OK" {
        val symbol = UUID.randomUUID().toString()
        val data = listOf(1.0, 2.0)
        val request = mapOf("symbol" to symbol, "values" to data)
        val expectedResponse = mapOf("symbol" to symbol)

        val addRequestBody = RequestBody.create(applicationJson, mapper.writeValueAsString(request))
        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/add_batch")
                .post(addRequestBody)
                .build()
        ).execute().apply {
            code shouldBe HttpStatus.OK.value()
            body?.string() shouldBe mapper.writeValueAsString(expectedResponse)
        }
    }

    "add_batch BAD REQUEST" {
        val request = mapOf<String, Any>()
        val addRequestBody = RequestBody.create(applicationJson, mapper.writeValueAsString(request))

        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/add_batch")
                .post(addRequestBody)
                .build()
        ).execute().code shouldBe HttpStatus.BAD_REQUEST.value()
    }

    "stats NOT FOUND" {
        val symbol = UUID.randomUUID().toString()

        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/stats?symbol=$symbol&k=2")
                .get()
                .build()
        ).execute().code shouldBe HttpStatus.NOT_FOUND.value()
    }

    "stats BAD REQUEST" {
        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/stats")
                .get()
                .build()
        ).execute().code shouldBe HttpStatus.BAD_REQUEST.value()
    }

    "stats one data OK" {
        val symbol = UUID.randomUUID().toString()
        val data = 2.123
        val request = mapOf("symbol" to symbol, "value" to data)
        val expectedStats = mapOf(
            "min" to data, "max" to data, "last" to data, "avg" to data, "var" to null
        )

        val addRequestBody = RequestBody.create(applicationJson, mapper.writeValueAsString(request))
        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/add")
                .post(addRequestBody)
                .build()
        ).execute()

        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/stats?symbol=$symbol&k=2")
                .get()
                .build()
        ).execute().apply {
            code shouldBe HttpStatus.OK.value()
            body?.string() shouldBe mapper.writeValueAsString(expectedStats)
        }
    }

    "stats multiple data OK" {
        val symbol = UUID.randomUUID().toString()
        val data = listOf(1.0, 2.0, 3.0, 4.0, 5.0)
        val request = mapOf("symbol" to symbol, "values" to data)
        val expectedStats = mapOf(
            "min" to 1.0, "max" to 5.0, "last" to 5.0, "avg" to 3.0, "var" to 2.0
        )

        val addRequestBody = RequestBody.create(applicationJson, mapper.writeValueAsString(request))
        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/add_batch")
                .post(addRequestBody)
                .build()
        ).execute()

        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/stats?symbol=$symbol&k=3")
                .get()
                .build()
        ).execute().apply {
            code shouldBe HttpStatus.OK.value()
            body?.string() shouldBe mapper.writeValueAsString(expectedStats)
        }
    }
})
