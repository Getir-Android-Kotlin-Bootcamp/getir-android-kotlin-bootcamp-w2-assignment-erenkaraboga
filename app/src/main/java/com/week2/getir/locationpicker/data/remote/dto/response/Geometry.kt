package com.week2.getir.locationpicker.data.remote.dto.response

data class Geometry(
    val bounds: Bounds,
    val location: Location,
    val location_type: String,
)