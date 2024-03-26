package com.week2.getir.locationpicker.data.repository


import com.week2.getir.locationpicker.data.remote.LocationPickerService
import com.week2.getir.locationpicker.data.remote.dto.response.ReverseAddressResponseModel
import com.week2.getir.locationpicker.domain.repository.LocationPickerRepository
import javax.inject.Inject

class LocationPickerRepositoryImpl @Inject constructor(private val api: LocationPickerService) : LocationPickerRepository {
    override suspend fun getReverseAddress(latlng: String): ReverseAddressResponseModel {
        //TODO API KEY KALDIR
        return api.getReverseAddress(latlng,"AIzaSyCNse2zVzctP__gWmpZmmgak7p8hLuEQ-0")
    }

}