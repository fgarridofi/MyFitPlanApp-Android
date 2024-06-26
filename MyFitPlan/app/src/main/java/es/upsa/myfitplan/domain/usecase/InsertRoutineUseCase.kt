package es.upsa.myfitplan.domain.usecase

import es.upsa.myfitplan.domain.model.routine.Routine
import es.upsa.myfitplan.domain.repository.LocalRepository
import javax.inject.Inject
import es.upsa.myfitplan.domain.Result

class InsertRoutineUseCase @Inject constructor(
    private val localRepository: LocalRepository
){
    fun execute(routine: Routine): Result<Unit> {
        return try {
            localRepository.saveRoutine(routine)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }

    }

}