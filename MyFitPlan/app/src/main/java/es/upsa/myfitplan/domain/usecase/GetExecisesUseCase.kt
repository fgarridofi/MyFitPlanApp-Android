package es.upsa.myfitplan.domain.usecase

import es.upsa.myfitplan.domain.repository.ApiRepository
import javax.inject.Inject
import es.upsa.myfitplan.domain.Result
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise

class GetExecisesUseCase @Inject constructor(
    private val apiRepository: ApiRepository
)
{
    suspend fun execute(): Result<List<MyFitPlanExercise>>{
        return try {
            val result = apiRepository.getExercises()
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}