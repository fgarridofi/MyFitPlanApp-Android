package es.upsa.myfitplan.domain.usecase

import es.upsa.myfitplan.domain.Result
import es.upsa.myfitplan.domain.model.routine.Routine
import es.upsa.myfitplan.domain.repository.LocalRepository
import javax.inject.Inject

class GetRoutineByIdUseCase @Inject constructor(
    private val localRepository: LocalRepository
){
    fun execute(routineId: Int) : Result<Routine>{
        return try {
            val result = localRepository.getRoutineById(routineId)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}