package com.example.a23_mju_mc_project

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class SentimentResponse(
    val document: Document
)

data class Document(
    val sentiment: String
)


// API 요청을 정의한 인터페이스
interface NaverApi {
    @Headers(
        "X-NCP-APIGW-API-KEY-ID: 232tu35wle",
        "X-NCP-APIGW-API-KEY: XYS84j8blmRXDcgHOcXGQb0VogJYqz8EfkSiiwRP",
        "Content-Type: application/json"
    )
    @POST("sentiment-analysis/v1/analyze")
    fun analyzeSentiment(@Body data: Map<String, String>): Call<SentimentResponse>
}

