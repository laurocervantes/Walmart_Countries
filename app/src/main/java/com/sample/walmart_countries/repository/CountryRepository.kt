package com.sample.walmart_countries.repository


import com.sample.walmart_countries.exception.CustomException
import com.sample.walmart_countries.model.Country
import com.sample.walmart_countries.service.CountryApi
import com.sample.walmart_countries.service.CountryApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class CountryRepository {

    var api: CountryApi = CountryApiFactory.countryApi // Use the factory

    suspend fun getCountries(): Result<List<Country>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCountries().execute()
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()
                    Result.success(countries)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown API error"
                    Result.failure(CustomException.ApiException(errorMessage))
                }
            } catch (e: IOException) {
                // Network-related exception
                Result.failure(CustomException.NetworkException("Network error", e))
            } catch (e: Exception) {
                // Generic exception
                Result.failure(CustomException.UnknownException("An unexpected error occurred", e))
            }
        }
    }
}