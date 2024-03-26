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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
import com.week2.getir.locationpicker.BuildConfig
import com.week2.getir.locationpicker.R
import com.week2.getir.locationpicker.databinding.ActivityLocationPickerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LocationPickerActivity : AppCompatActivity(), OnMapReadyCallback {
    private var REQUEST_CODE_LOCATION =100
    private val viewModel: LocationPickerViewModel by viewModels()
    private lateinit var binding: ActivityLocationPickerBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setUpListeners()
        observeReverseAddress()
        checkPermissions()
    }

    private fun initializeViews() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Places.initialize(applicationContext, BuildConfig.PLACES_KEY)
    }

    private fun setUpListeners() {
        binding.etSearch.setOnClickListener { startLocationSearch() }
        binding.btNext.setOnClickListener { onNextButtonClicked() }
    }

    private fun startLocationSearch() {
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY,
            listOf(
                Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS
            )
        ).build(this)
        startForResult.launch(intent)
    }

    private fun onNextButtonClicked() {
        map?.cameraPosition?.target?.let { place ->
            addMarkerToLocation(place)
            viewModel.getReverseAddress(combineCoordinates(place))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    private fun observeReverseAddress() {
        viewModel.getReverseAddressViewState()
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: LocationPickerViewModel.ReverseAddressViewState) {
        if (state is LocationPickerViewModel.ReverseAddressViewState.Success && state.data.results.isNotEmpty()) {
            binding.tvAddress.text = state.data.results[0].formatted_address
        }
    }

    private fun checkPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                Toast.makeText(this, getString(R.string.location_access_granted), Toast.LENGTH_SHORT).show()
                getLocation()
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
    private fun getLocation() {
        if (isLocationEnabled()) {
            val result = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            )
            result.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = LatLng(task.result.latitude, task.result.longitude)
                    animateCamera(location)
                    addMarkerToLocation(location)
                    viewModel.getReverseAddress(combineCoordinates(location))
                } else {
                    Toast.makeText(this, getString(R.string.failed_to_get_location), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            showLocationServicePopup()
        }
    }
    private fun animateCamera(latLng: LatLng) {
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
    }

    private fun addMarkerToLocation(latLng: LatLng) {
        map?.clear()
        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_map_pin)
        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 500, 500, false)
        val icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap)
        map?.addMarker(MarkerOptions().position(latLng).icon(icon))
    }

    private fun showLocationServicePopup() {
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000
        ).setMinUpdateIntervalMillis(5000).build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this, 100)
                } catch (_: java.lang.Exception) {
                }
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val place = Autocomplete.getPlaceFromIntent(result.data!!)
            place.latLng?.let { viewModel.getReverseAddress(combineCoordinates(it)) }
            place.latLng?.let { animateCamera(it) }
            binding.tvLocation.text = place.address
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, getString(R.string.you_did_not_make_choice), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                getLocation()
            } else {
                Toast.makeText(this, getString(R.string.you_did_not_make_choice), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun combineCoordinates(latLng: LatLng): String {
        return "${latLng.latitude},${latLng.longitude}"
    }

    companion object {
        fun createSimpleIntent(context: Context): Intent =
            Intent(context, LocationPickerActivity::class.java)
    }
}