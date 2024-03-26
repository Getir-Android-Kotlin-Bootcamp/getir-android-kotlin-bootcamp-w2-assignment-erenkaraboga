package com.week2.getir.locationpicker.di

import com.week2.getir.locationpicker.data.remote.LocationPickerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    //TODO BASE_URL KALDIR
    @Provides
    @Singleton
    fun provideRegisterService(okHttpClient: OkHttpClient): LocationPickerService {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationPickerService::class.java)
    }


    }
