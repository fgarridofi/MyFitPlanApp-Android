package es.upsa.myfitplan.domain.usecase

import es.upsa.myfitplan.domain.model.routine.Routine
import es.upsa.myfitplan.domain.repository.LocalRepository
import javax.inject.Inject
import es.upsa.myfitplan.domain.Result

class GetRoutinesUseCase @Inject constructor(
    private val localRepository: LocalRepository
){

        fun execute(): Result<List<Routine>> {
            return try {
                val result = localRepository.getRoutines()
                Result.Success(result)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

}