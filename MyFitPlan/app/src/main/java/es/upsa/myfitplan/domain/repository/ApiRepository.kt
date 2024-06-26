package es.upsa.myfitplan.domain.repository

import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise

interface ApiRepository {

    suspend fun getExercises(): List<MyFitPlanExercise>

    suspend fun getExerciseById(id: String): MyFitPlanExercise

    suspend fun getExercisesByName(name: String): List<MyFitPlanExercise>
}