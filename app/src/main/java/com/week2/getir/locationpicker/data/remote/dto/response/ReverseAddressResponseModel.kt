package com.week2.getir.locationpicker.data.remote.dto.response

data class ReverseAddressResponseModel(
    val plus_code: PlusCode,
    val results: List<Result>,
    val status: String
)