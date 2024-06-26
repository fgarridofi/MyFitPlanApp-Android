package es.upsa.myfitplan.data.repository

import es.upsa.myfitplan.data.api.ExercisesApi
import es.upsa.myfitplan.data.mapper.ExerciseMapper
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise
import es.upsa.myfitplan.domain.repository.ApiRepository
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val exercisesApi: ExercisesApi
) : ApiRepository {


    override suspend fun getExercises(): List<MyFitPlanExercise> {
        val response = exercisesApi.getExercises()
        return ExerciseMapper.mapListExerciseToDomain(response)
    }

    override suspend fun getExerciseById(id: String): MyFitPlanExercise {
        val response = exercisesApi.getExerciseById(id)
        return ExerciseMapper.mapExerciseToDomain(response)
    }

    override suspend fun getExercisesByName(name: String): List<MyFitPlanExercise> {
        val response = exercisesApi.getExercisesByName(name)
        return ExerciseMapper.mapListExerciseToDomain(response)
    }

}