package es.upsa.myfitplan.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.upsa.myfitplan.data.api.ExercisesApi
import es.upsa.myfitplan.data.db.RoutinesDao
import es.upsa.myfitplan.domain.repository.ApiRepository
import es.upsa.myfitplan.data.repository.ApiRepositoryImpl
import es.upsa.myfitplan.data.repository.LocalRepositoryImpl
import es.upsa.myfitplan.domain.repository.LocalRepository
import es.upsa.myfitplan.data.db.DataBase
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiRepository(
        api: ExercisesApi
    ): ApiRepository = ApiRepositoryImpl(api)

    @Provides
    @Singleton
    fun providesLocalRepository(
        routinesDao: RoutinesDao
    ): LocalRepository = LocalRepositoryImpl(routinesDao)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): DataBase {
        return Room.databaseBuilder(
            appContext,
            DataBase::class.java,
            "myfitplan_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    fun provideRoutinesDao(database: DataBase): RoutinesDao {
        return database.routinesDao()
    }
}