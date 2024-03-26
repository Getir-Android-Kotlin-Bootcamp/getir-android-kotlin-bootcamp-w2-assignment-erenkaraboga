package com.week2.getir.locationpicker.data.repository


import com.week2.getir.locationpicker.BuildConfig
import com.week2.getir.locationpicker.data.remote.LocationPickerService
import com.week2.getir.locationpicker.data.remote.dto.response.ReverseAddressResponseModel
import com.week2.getir.locationpicker.domain.repository.LocationPickerRepository
import javax.inject.Inject

class LocationPickerRepositoryImpl @Inject constructor(private val api: LocationPickerService) : LocationPickerRepository {
    override suspend fun getReverseAddress(latlng: String): ReverseAddressResponseModel {
        return api.getReverseAddress(latlng,BuildConfig.REVERSE_KEY)
    }

}