package es.upsa.myfitplan.domain.usecase

import es.upsa.myfitplan.domain.Result
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise
import es.upsa.myfitplan.domain.repository.ApiRepository
import javax.inject.Inject

class GetExercisesByNameUseCase @Inject constructor(
    private val apiRepository: ApiRepository
){
    suspend fun execute(name: String) : Result<List<MyFitPlanExercise>> {
        return try {
            val result = apiRepository.getExercisesByName(name)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}