package com.wcsm.shopperrotas.di

import com.wcsm.shopperrotas.data.remote.api.ShopperAPI
import com.wcsm.shopperrotas.data.repository.IRideRepository
import com.wcsm.shopperrotas.data.repository.RideRepositoryImpl
import com.wcsm.shopperrotas.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideShopperAPI(retrofit: Retrofit) : ShopperAPI {
        return retrofit.create(ShopperAPI::class.java)
    }

    @Provides
    fun provideTravelRepository(shopperAPI: ShopperAPI) : IRideRepository {
        return RideRepositoryImpl(shopperAPI)
    }

}