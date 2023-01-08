package com.example.mobiledevfinal.repositories

import com.example.mobiledevfinal.models.Quote
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface QuotesApi {
    @GET("/random")
    suspend fun getRandomQuote(): Response<Quote>
}

object QuotesRepository {
    private val quotesApi: QuotesApi = Retrofit.Builder()
        .baseUrl("https://quotable.io")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuotesApi::class.java)

    suspend fun  getDailyQuote(): Quote {
        val result = quotesApi.getRandomQuote()
        val quote = result.body()
        quote ?: throw RuntimeException("An error occured")
        return quote
    }
}