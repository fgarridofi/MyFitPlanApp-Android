package es.upsa.myfitplan.presentation.screens.homeRoutine

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.upsa.myfitplan.domain.usecase.GetRoutinesUseCase
import es.upsa.myfitplan.domain.usecase.InsertRoutineUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import es.upsa.myfitplan.domain.Result
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise
import es.upsa.myfitplan.domain.model.routine.Routine
import es.upsa.myfitplan.domain.model.series.Series
import es.upsa.myfitplan.domain.usecase.AddSeriesUseCase
import es.upsa.myfitplan.domain.usecase.DeleteRoutineUseCase
import es.upsa.myfitplan.domain.usecase.GetRoutineByIdUseCase
import es.upsa.myfitplan.domain.usecase.UpdateSeriesUseCase
import kotlinx.coroutines.flow.StateFlow


@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val InsertRoutineUseCase: InsertRoutineUseCase,
    private val GetRoutinesUseCase: GetRoutinesUseCase,
    private val DeleteRoutineUseCase: DeleteRoutineUseCase,
    private val GetRoutineByIdUseCase: GetRoutineByIdUseCase,
    private val addSeriesUseCase: AddSeriesUseCase,
    private val updateSeriesUseCase: UpdateSeriesUseCase


    ) : ViewModel() {

    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines = _routines.asStateFlow()

    private val _selectedRoutine = MutableStateFlow<Routine?>(null)
    val selectedRoutine: StateFlow<Routine?> = _selectedRoutine.asStateFlow()


    init {
        loadRoutines()
    }


    fun loadRoutines(selectedRoutineId: Int? = null, onComplete: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = GetRoutinesUseCase.execute()) {
                is Result.Success -> {
                    _routines.value = result.data
                    if (selectedRoutineId != null) {
                        _selectedRoutine.value = result.data.find { it.id == selectedRoutineId }
                    }
                    onComplete()
                }

                is Result.Error -> Log.d(
                    "RoutineViewModel",
                    "Error loading routines: ${result.exception.message}"
                )
            }
        }
    }


    fun insertRoutine(routine: Routine) {
        viewModelScope.launch(Dispatchers.IO) {
            // Add a base series to each exercise if it has no series
            val updatedExercises = routine.exercises.map { exercise ->
                if (exercise.series.isEmpty()) {
                    exercise.copy(series = listOf(Series(num = 1, reps = null, weight = null)))
                } else {
                    exercise
                }
            }

            val updatedRoutine = routine.copy(exercises = updatedExercises)


            InsertRoutineUseCase.execute(updatedRoutine).let { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d("RoutineViewModel", "Routine inserted")
                        loadRoutines()
                    }

                    is Result.Error -> {
                        Log.d("RoutineViewModel", "Error: ${result.exception.message}")
                    }
                }
            }
        }
    }

    fun deleteRoutine(routineId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            DeleteRoutineUseCase.execute(routineId).let { result ->
                if (result is Result.Success) {
                    Log.d("RoutineViewModel", "Routine deleted")
                    loadRoutines()
                } else {
                    Log.d(
                        "RoutineViewModel",
                        "Error deleting routine: ${(result as Result.Error).exception.message}"
                    )
                }
            }
        }
    }

    fun getRoutineById(routineId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetRoutineByIdUseCase.execute(routineId)
            if (result is Result.Success) {
                _selectedRoutine.value = result.data
                Log.d("RoutineViewModel", "${result.data}")
            } else if (result is Result.Error) {
                Log.d(
                    "RoutineViewModel",
                    "Error getting routine by id: ${result.exception.message}"
                )
                _selectedRoutine.value = null
            }
        }
    }

    fun addSeriesToExercise(routineId: Int, exerciseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newSeries = Series(
                num = determineNextSeriesNumber(routineId, exerciseId),
                reps = "0",
                weight = "0"
            )
            val result = addSeriesUseCase.execute(routineId, exerciseId, newSeries)
            if (result is Result.Success) {
                loadRoutines(routineId)
            } else if (result is Result.Error) {
                Log.e("SeriesViewModel", "Error adding series", result.exception)
            }
        }
    }

    private fun determineNextSeriesNumber(routineId: Int, exerciseId: String): Int {
        val routine = routines.value.find { it.id == routineId }
        val exercise = routine?.exercises?.find { it.id == exerciseId }
        return (exercise?.series?.maxOfOrNull { it.num } ?: 1) + 1
    }



    fun updateSeries(routineId: Int, exerciseId: String, seriesNum: Int, reps: String?, weight: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentRoutine = _routines.value.firstOrNull { it.id == routineId }
            val currentExercise = currentRoutine?.exercises?.firstOrNull { it.id == exerciseId }
            val currentSeries = currentExercise?.series?.firstOrNull { it.num == seriesNum }

            val updatedReps = reps ?: currentSeries?.reps
            val updatedWeight = weight ?: currentSeries?.weight

            val updatedSeries = Series(num = seriesNum, reps = updatedReps ?: "0", weight = updatedWeight ?: "0")

            val result = updateSeriesUseCase.execute(routineId, exerciseId, updatedSeries)
            if (result is Result.Success) {
                loadRoutines()
            } else if (result is Result.Error) {
                Log.e("RoutineViewModel", "Error updating series", result.exception)
            }
        }
    }

}
