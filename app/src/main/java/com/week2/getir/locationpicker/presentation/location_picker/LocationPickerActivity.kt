package com.week2.getir.locationpicker.presentation.location_picker

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.week2.getir.locationpicker.R
import com.week2.getir.locationpicker.databinding.ActivityLocationPickerBinding


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
        initAutoComplete()
        listener()
    }

    private fun initMapView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initLocationService() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }
    private fun initAutoComplete(){
        Places.initialize(applicationContext,getString(R.string.map_key))
    }

   private  fun listener(){
       binding.etSearch.setOnClickListener{
           val intent = Autocomplete.IntentBuilder(
               AutocompleteActivityMode.OVERLAY, listOf(Place.Field.ID,Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.ADDRESS_COMPONENTS)
           ).build(this)
           startForResult.launch(intent)
       }
   }
    private fun animateCamera(latLng: LatLng){
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f))
    }
    private fun addMarkerToLocation(latLng: LatLng){
        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_map_pin)
        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 500, 500, false)
        val icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap)
        map?.addMarker(MarkerOptions().position(latLng).icon(icon))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
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
               animateCamera(location)
               addMarkerToLocation(location)
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

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK && data != null) {
                val place = Autocomplete.getPlaceFromIntent(data)
                place.latLng?.let { animateCamera(it) }
                binding.tvLocation.text = place.address
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "You didn't make a choice ", Toast.LENGTH_SHORT).show()
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
    fun resizeBitmap(drawableName: String?, width: Int, height: Int): Bitmap? {
        val imageBitmap = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                drawableName, "drawable",
                packageName
            )
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    companion object {
        fun createSimpleIntent(context: Context): Intent =
            Intent(context, LocationPickerActivity::class.java)
    }
}