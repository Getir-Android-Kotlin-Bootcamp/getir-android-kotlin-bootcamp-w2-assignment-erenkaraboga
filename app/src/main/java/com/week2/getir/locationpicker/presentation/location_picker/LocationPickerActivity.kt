package com.week2.getir.locationpicker.presentation.location_picker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.week2.getir.locationpicker.R
import com.week2.getir.locationpicker.databinding.ActivityLocationPickerBinding

class LocationPickerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLocationPickerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        fun createSimpleIntent(context: Context): Intent =
            Intent(context, LocationPickerActivity::class.java)
    }
}