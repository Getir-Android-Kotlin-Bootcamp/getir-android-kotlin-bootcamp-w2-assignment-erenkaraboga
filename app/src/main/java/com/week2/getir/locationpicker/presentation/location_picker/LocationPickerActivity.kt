package com.week2.getir.locationpicker.presentation.location_picker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.week2.getir.locationpicker.R
import com.week2.getir.locationpicker.databinding.ActivityLocationPickerBinding
import java.lang.Exception

class LocationPickerActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityLocationPickerBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var map: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMapView()
        initLocationService()
        checkPermissions()
    }

    private fun initMapView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initLocationService() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    @SuppressLint("MissingPermission")
    private fun checkPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                        permissions.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            false
                        ) -> {
                    Toast.makeText(this, "Location access granted", Toast.LENGTH_SHORT).show()
                    getLocation()
                }
            }
        }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

    }
    @SuppressLint("MissingPermission")
   private fun getLocation(){
       if (isLocationEnabled()) {
           val result = fusedLocationClient.getCurrentLocation(
               Priority.PRIORITY_BALANCED_POWER_ACCURACY,
               CancellationTokenSource().token
           )
           result.addOnCompleteListener{
               val location = LatLng(it.result.latitude,it.result.longitude)
               Toast.makeText(this, "${result.result.latitude}  ${result.result.longitude}", Toast.LENGTH_SHORT).show()
               animateCamera(location)
           }
       }
       else{
           Toast.makeText(this, "Please turn on location", Toast.LENGTH_SHORT).show()
           createLocationServicePopUp()
       }
    }
    private fun createLocationServicePopUp(){
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,10000
        ).setMinUpdateIntervalMillis(5000).build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task= client.checkLocationSettings(builder.build())
        task.addOnFailureListener{e ->
            if(e is ResolvableApiException){
                try {
                    e.startResolutionForResult(this,100)
                }catch(_: java.lang.Exception){}
            }
        }
    }
    private fun isLocationEnabled():Boolean{
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return false
    }
    private fun animateCamera(latLng: LatLng){
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f))
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        fun createSimpleIntent(context: Context): Intent =
            Intent(context, LocationPickerActivity::class.java)
    }
}