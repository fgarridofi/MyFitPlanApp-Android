package es.upsa.myfitplan.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import es.upsa.myfitplan.BuildConfig
import es.upsa.myfitplan.data.api.ExercisesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-RapidAPI-Key", BuildConfig.API_KEY)
                    .addHeader("X-RapidAPI-Host", BuildConfig.API_HOST)

                return@addInterceptor chain.proceed(request.build())
            }
            .build()

    }


    @Provides
    fun providesRetrofit(httpClient: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .client(httpClient)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create()
            )
        )


    @Provides
    fun providesApiInterface(retrofitBuilder: Retrofit.Builder): ExercisesApi =
        retrofitBuilder.baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(ExercisesApi::class.java)
    }

