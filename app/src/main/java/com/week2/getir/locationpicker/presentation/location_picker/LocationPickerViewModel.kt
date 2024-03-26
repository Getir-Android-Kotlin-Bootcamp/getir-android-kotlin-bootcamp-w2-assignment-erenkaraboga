package com.week2.getir.locationpicker.presentation.location_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.week2.getir.locationpicker.common.Resource
import com.week2.getir.locationpicker.common.UiText
import com.week2.getir.locationpicker.data.remote.dto.response.ReverseAddressResponseModel
import com.week2.getir.locationpicker.domain.usecase.LocationPickerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LocationPickerViewModel @Inject constructor(

    private val locationPickerUseCase: LocationPickerUseCase
) : ViewModel() {


    private val reverseAddressViewState = MutableStateFlow<ReverseAddressViewState>(
        ReverseAddressViewState.Init
    )

    fun getReverseAddressViewState(): StateFlow<ReverseAddressViewState> = reverseAddressViewState.asStateFlow()


    fun getReverseAddress(period : String) {
        locationPickerUseCase.invoke(period).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    reverseAddressViewState.value =
                        ReverseAddressViewState.Error(error = result.message)
                }
                is Resource.Loading -> Unit
                is Resource.Success -> {
                    if (result.data != null) reverseAddressViewState.value =
                        ReverseAddressViewState.Success(result.data)
                    else {
                        reverseAddressViewState.value =
                            ReverseAddressViewState.Error(error = result.message)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    sealed class ReverseAddressViewState {
        data object Init : ReverseAddressViewState()
        data class IsLoading(val isLoading: Boolean) : ReverseAddressViewState()
        data class Success(val data: ReverseAddressResponseModel) : ReverseAddressViewState()
        data class Error(val error: UiText) : ReverseAddressViewState()
    }
}