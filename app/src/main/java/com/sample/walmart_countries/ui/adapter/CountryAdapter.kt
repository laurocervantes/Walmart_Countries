package com.sample.walmart_countries.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.walmart_countries.R
import com.sample.walmart_countries.model.Country

class CountryAdapter(private var countries: List<Country>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameRegionTextView: TextView = itemView.findViewById(R.id.nameRegionTextView)
        val codeTextView: TextView = itemView.findViewById(R.id.codeTextView)
        val capitalTextView: TextView = itemView.findViewById(R.id.capitalTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_item, parent, false) // Use the correct layout
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.nameRegionTextView.text = "${country.name}, ${country.region}"
        holder.codeTextView.text = country.code
        holder.capitalTextView.text = country.capital
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCountries: List<Country>) {
        countries = newCountries
        notifyDataSetChanged()
    }
}
