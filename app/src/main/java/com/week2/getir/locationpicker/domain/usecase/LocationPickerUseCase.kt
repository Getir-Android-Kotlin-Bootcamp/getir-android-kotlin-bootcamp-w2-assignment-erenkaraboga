package com.week2.getir.locationpicker.domain.usecase

import com.week2.getir.locationpicker.R
import com.week2.getir.locationpicker.common.Resource
import com.week2.getir.locationpicker.common.UiText
import com.week2.getir.locationpicker.common.extensions.handleError
import com.week2.getir.locationpicker.data.remote.dto.response.ReverseAddressResponseModel
import com.week2.getir.locationpicker.domain.repository.LocationPickerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class LocationPickerUseCase @Inject constructor(
    private val repository: LocationPickerRepository
) {
    operator fun invoke(latlng: String): Flow<Resource<ReverseAddressResponseModel>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.getReverseAddress(latlng)
            emit(Resource.Success(data = response))
        } catch (e: HttpException) {
            emit(Resource.Error(e.handleError()))
        } catch (e: IOException) {
            emit(Resource.Error(UiText.StringResource(R.string.Error)))
        }
    }
}