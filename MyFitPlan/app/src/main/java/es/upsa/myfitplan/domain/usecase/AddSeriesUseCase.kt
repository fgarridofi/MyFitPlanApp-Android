package es.upsa.myfitplan.domain.usecase

import es.upsa.myfitplan.domain.Result
import es.upsa.myfitplan.domain.model.routine.Routine
import es.upsa.myfitplan.domain.model.series.Series
import es.upsa.myfitplan.domain.repository.LocalRepository
import javax.inject.Inject

class AddSeriesUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    fun execute(routineId: Int, exerciseId: String, newSeries: Series): Result<Unit> {
        val routine = localRepository.getRoutineById(routineId)
        val updatedExercises = routine.exercises.map { exercise ->
            if (exercise.id == exerciseId) {
                exercise.copy(series = exercise.series + newSeries)
            } else {
                exercise
            }
        }
        val updatedRoutine = routine.copy(exercises = updatedExercises)
        return try {
            localRepository.updateRoutine(updatedRoutine)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}