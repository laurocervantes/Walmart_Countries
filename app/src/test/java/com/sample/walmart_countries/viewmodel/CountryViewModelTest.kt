package com.sample.walmart_countries.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sample.walmart_countries.model.Country
import com.sample.walmart_countries.repository.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CountryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repository: CountryRepository

    @Mock
    private lateinit var countriesObserver: Observer<List<Country>>

    @Mock
    private lateinit var errorMessageObserver: Observer<String>

    private lateinit var viewModel: CountryViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        viewModel = CountryViewModel().apply {
            // Replace the repository with the mock
            this.repository = this@CountryViewModelTest.repository
            countries.observeForever(countriesObserver)
            errorMessage.observeForever(errorMessageObserver)
        }
    }

    @Test
    fun `loadCountries success updates countries LiveData`() = runTest {
        // Arrange
        val mockCountries = listOf(
            Country("United States", "NA", "US", "Washington, D.C."),
            Country("Canada", "NA", "CA", "Ottawa")
        )
        `when`(repository.getCountries()).thenReturn(Result.success(mockCountries))

        // Act
        viewModel.loadCountries()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(countriesObserver).onChanged(mockCountries)
    }

    @Test
    fun `loadCountries failure updates errorMessage LiveData`() = runTest {
        // Arrange
        val errorMessage = "Failed to load countries"
        `when`(repository.getCountries()).thenReturn(Result.failure(Exception(errorMessage)))

        // Act
        viewModel.loadCountries()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(errorMessageObserver).onChanged(errorMessage)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.countries.removeObserver(countriesObserver)
        viewModel.errorMessage.removeObserver(errorMessageObserver)
    }
}