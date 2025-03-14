package com.sample.walmart_countries.repository

import com.sample.walmart_countries.exception.CustomException
import com.sample.walmart_countries.model.Country
import com.sample.walmart_countries.service.CountryApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class CountryRepositoryTest {

    @Mock
    private lateinit var countryApi: CountryApi

    private lateinit var repository: CountryRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = CountryRepository().apply { api = countryApi }
    }

    @Test
    fun `getCountries returns success with data`(): Unit = runBlocking {
        // Arrange
        val mockCountries = listOf(
            Country("United States", "NA", "US", "Washington, D.C."),
            Country("Canada", "NA", "CA", "Ottawa")
        )
        val mockResponse: Response<List<Country>> = Response.success(mockCountries)
        val mockCall: Call<List<Country>> = mock(Call::class.java) as Call<List<Country>>
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(countryApi.getCountries()).thenReturn(mockCall)

        // Act
        val result = repository.getCountries()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(mockCountries, result.getOrNull())
        verify(countryApi).getCountries()
        verify(mockCall).execute()
    }

    @Test
    fun `getCountries returns ApiException on API error`(): Unit = runBlocking {
        // Arrange
        val errorMessage = "API Error"
        val errorResponse: Response<List<Country>> = Response.error(500, errorMessage.toResponseBody("application/json".toMediaTypeOrNull()))
        val mockCall: Call<List<Country>> = mock(Call::class.java) as Call<List<Country>>
        `when`(mockCall.execute()).thenReturn(errorResponse)
        `when`(countryApi.getCountries()).thenReturn(mockCall)

        // Act
        val result = repository.getCountries()

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is CustomException.ApiException)
        assertEquals(errorMessage, (result.exceptionOrNull() as CustomException.ApiException).message)
        verify(countryApi).getCountries()
        verify(mockCall).execute()
    }

    @Test
    fun `getCountries returns NetworkException on IOException`(): Unit = runBlocking {
        // Arrange
        val exceptionMessage = "Network error"
        val mockCall: Call<List<Country>> = mock(Call::class.java) as Call<List<Country>>
        `when`(mockCall.execute()).thenThrow(IOException(exceptionMessage))
        `when`(countryApi.getCountries()).thenReturn(mockCall)

        // Act
        val result = repository.getCountries()

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is CustomException.NetworkException)
        assertEquals(exceptionMessage, (result.exceptionOrNull() as CustomException.NetworkException).message)
        verify(countryApi).getCountries()
        verify(mockCall).execute()
    }

    @Test
    fun `getCountries returns UnknownException on generic exception`(): Unit = runBlocking {
        // Arrange
        val exceptionMessage = "Generic error"
        val mockCall: Call<List<Country>> = mock(Call::class.java) as Call<List<Country>>
        `when`(mockCall.execute()).thenThrow(RuntimeException(exceptionMessage))
        `when`(countryApi.getCountries()).thenReturn(mockCall)

        // Act
        val result = repository.getCountries()

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is CustomException.UnknownException)
        assertEquals("An unexpected error occurred", (result.exceptionOrNull() as CustomException.UnknownException).message)
        verify(countryApi).getCountries()
        verify(mockCall).execute()
    }
}