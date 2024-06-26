package es.upsa.myfitplan.domain.usecase

import android.util.Log
import es.upsa.myfitplan.domain.Result
import es.upsa.myfitplan.domain.model.routine.Routine
import es.upsa.myfitplan.domain.model.series.Series
import es.upsa.myfitplan.domain.repository.LocalRepository
import javax.inject.Inject

class UpdateSeriesUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    fun execute(routineId: Int, exerciseId: String, updatedSeries: Series): Result<Unit> {
        val routine = localRepository.getRoutineById(routineId)
        val updatedExercises = routine.exercises.map { exercise ->
            if (exercise.id == exerciseId) {
                val newSeriesList = exercise.series.map { series ->
                    if (series.num == updatedSeries.num) updatedSeries else series
                }
                exercise.copy(series = newSeriesList)
            } else {
                exercise
            }
        }
        val updatedRoutine = routine.copy(exercises = updatedExercises)
        Log.d("UpdateSeriesUseCase", "Updating routine: $updatedRoutine")
        return try {
            localRepository.updateRoutine(updatedRoutine)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}