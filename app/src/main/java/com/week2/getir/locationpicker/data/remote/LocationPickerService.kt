package com.week2.getir.locationpicker.data.remote

import com.week2.getir.locationpicker.data.remote.dto.response.ReverseAddressResponseModel
import retrofit2.http.*

interface LocationPickerService {

    @GET("/maps/api/geocode/json")
    suspend fun getReverseAddress(
        @Query("latlng") latlng: String,
        @Query("key") key: String,
    ): ReverseAddressResponseModel
}

