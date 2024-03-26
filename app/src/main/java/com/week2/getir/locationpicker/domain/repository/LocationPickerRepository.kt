package com.week2.getir.locationpicker.domain.repository

import com.week2.getir.locationpicker.data.remote.dto.response.ReverseAddressResponseModel


interface LocationPickerRepository {
    suspend fun getReverseAddress(latlng:String): ReverseAddressResponseModel
}