package com.week2.getir.locationpicker.common.extensions

import com.google.gson.Gson
import com.week2.getir.locationpicker.R
import com.week2.getir.locationpicker.common.UiText
import com.week2.getir.locationpicker.data.remote.dto.response.ErrorModelDto
import retrofit2.HttpException
val gson = Gson()
fun HttpException.handleError(): UiText {
    val errorString = this.response()?.errorBody()?.string()
    errorString?.let {
        val errorModel = gson.fromJson(errorString, ErrorModelDto::class.java)
        if (errorModel?.message != null)
            return UiText.DynamicString(errorModel.message)
        else
            return UiText.StringResource(R.string.Error)
    }
    return this.localizedMessage?.let { UiText.DynamicString(it) }
        ?: UiText.StringResource(R.string.Error)
}