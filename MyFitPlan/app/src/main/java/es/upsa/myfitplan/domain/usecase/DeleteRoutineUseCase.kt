package es.upsa.myfitplan.domain.usecase

import es.upsa.myfitplan.domain.repository.LocalRepository
import javax.inject.Inject
import es.upsa.myfitplan.domain.Result

class DeleteRoutineUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend fun execute(routineId: Int): Result<Unit> {
        return try {
            localRepository.deleteRoutine(routineId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}