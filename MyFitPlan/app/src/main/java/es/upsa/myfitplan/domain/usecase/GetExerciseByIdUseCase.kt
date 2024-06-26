package es.upsa.myfitplan.domain.usecase

import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise
import es.upsa.myfitplan.domain.repository.ApiRepository
import javax.inject.Inject
import es.upsa.myfitplan.domain.Result

class GetExerciseByIdUseCase @Inject constructor(
    private val apiRepository: ApiRepository
){
    suspend fun execute(id: String) : Result<MyFitPlanExercise> {
        return try {
            val result = apiRepository.getExerciseById(id)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }


}