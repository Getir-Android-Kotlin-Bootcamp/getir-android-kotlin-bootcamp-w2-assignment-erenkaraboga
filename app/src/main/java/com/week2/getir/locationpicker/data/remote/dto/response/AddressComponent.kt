package com.week2.getir.locationpicker.data.remote.dto.response

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)