package com.week2.getir.locationpicker.di

import com.week2.getir.locationpicker.data.remote.LocationPickerService
import com.week2.getir.locationpicker.data.repository.LocationPickerRepositoryImpl
import com.week2.getir.locationpicker.domain.repository.LocationPickerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideLocationPickerRepository(apiService: LocationPickerService): LocationPickerRepository =
        LocationPickerRepositoryImpl(apiService)

}

