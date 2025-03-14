package com.sample.walmart_countries

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.walmart_countries.databinding.ActivityMainBinding
import com.sample.walmart_countries.ui.adapter.CountryAdapter
import com.sample.walmart_countries.viewmodel.CountryViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CountryAdapter
    private lateinit var viewModel: CountryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize ViewModel
        viewModel = ViewModelProvider(this)[CountryViewModel::class.java]
        binding.viewModel = viewModel //binding viewModel
        binding.lifecycleOwner = this // Important for LiveData observation in XML

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CountryAdapter(emptyList())
        recyclerView.adapter = adapter

        viewModel.countries.observe(this) { countries ->
            adapter.updateData(countries)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            Log.e("MainActivity", "Error: $errorMessage")
        }

    }

}
