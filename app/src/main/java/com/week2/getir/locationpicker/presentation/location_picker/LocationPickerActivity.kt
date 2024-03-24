package com.week2.getir.locationpicker.presentation.location_picker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.week2.getir.locationpicker.R
import com.week2.getir.locationpicker.databinding.ActivityLocationPickerBinding

class LocationPickerActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var binding: ActivityLocationPickerBinding
    private  var map : GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMapView()
    }
    private fun initMapView(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }
    companion object {
        fun createSimpleIntent(context: Context): Intent =
            Intent(context, LocationPickerActivity::class.java)
    }
}