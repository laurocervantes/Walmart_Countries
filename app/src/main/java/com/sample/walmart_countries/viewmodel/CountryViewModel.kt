package com.sample.walmart_countries.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.walmart_countries.model.Country
import com.sample.walmart_countries.repository.CountryRepository
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {

    var repository = CountryRepository()

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> = _countries

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        loadCountries()
    }

    fun loadCountries() {
        viewModelScope.launch {
            val result = repository.getCountries()
            if (result.isSuccess) {
                _countries.value = result.getOrNull() ?: emptyList()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Unknown error"
            }
        }
    }
}
