package com.week2.getir.locationpicker.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.week2.getir.locationpicker.R
import com.week2.getir.locationpicker.presentation.location_picker.LocationPickerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigate()
    }

    private fun navigate() {
        finish()
        startActivity(LocationPickerActivity.
        createSimpleIntent(this@MainActivity))
    }
}