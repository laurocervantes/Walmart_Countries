package com.sample.walmart_countries.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CountryApiFactory {
    val countryApi: CountryApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryApi::class.java)
    }
}