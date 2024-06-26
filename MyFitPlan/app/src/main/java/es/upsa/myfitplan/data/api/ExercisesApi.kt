package es.upsa.myfitplan.data.api

import es.upsa.myfitplan.data.model.exercises.Exercise
import retrofit2.http.GET
import retrofit2.http.Path

interface ExercisesApi {

    @GET("exercises?limit=1400")
    suspend fun getExercises(): List<Exercise>

    @GET("exercises/name/{name}")
    suspend fun getExercisesByName(@Path("name")name: String): List<Exercise>

    @GET("exercises/exercise/{id}")
    suspend fun getExerciseById(@Path("id")id: String): Exercise
}