package com.example.androidapp.di



import android.content.Context
import androidx.room.Room
import com.example.androidapp.networkService.CommonApiService
import com.example.androidapp.networkService.OkHttpClientHelper
import com.example.androidapp.utils.Constants
import com.example.androidapp.home.util.HomeRepository
import com.example.androidapp.roomDb.AppDatabase
import com.example.androidapp.roomDb.AppDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesCommonApiService(): CommonApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(OkHttpClientHelper().getOkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommonApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context):AppDatabase
            = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_db"
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideNotesDao(noteDatabase: AppDatabase):AppDatabaseDao
            =noteDatabase.appDao()

}