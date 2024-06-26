package es.upsa.myfitplan.presentation.screens.exercises

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.upsa.myfitplan.domain.usecase.GetExecisesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import es.upsa.myfitplan.domain.Result
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise
import es.upsa.myfitplan.domain.usecase.GetExerciseByIdUseCase
import es.upsa.myfitplan.domain.usecase.GetExercisesByNameUseCase

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val getExecisesUseCase: GetExecisesUseCase,
    private val getExerciseByIdUseCase: GetExerciseByIdUseCase,
    private val getExercisesByNameUseCase: GetExercisesByNameUseCase
): ViewModel() {

    private val _exercises : MutableStateFlow<List<MyFitPlanExercise>> = MutableStateFlow(emptyList())
    val exercises = _exercises.asStateFlow()

    private val _exerciseDetails = MutableStateFlow<MyFitPlanExercise?>(null)
    val exerciseDetails = _exerciseDetails.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getExercises()
    }

    private fun getExercises(){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            getExecisesUseCase.execute().let { result ->
                when (result) {
                    is Result.Success -> {
                        _exercises.value = result.data
                    }
                    is Result.Error -> {
                        Log.d("ExercisesViewModel", "Error: ${result.exception.message}")
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun getExerciseById(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            getExerciseByIdUseCase.execute(id).let { result ->
                when (result) {
                    is Result.Success -> {
                        _exerciseDetails.value = result.data                    }
                    is Result.Error -> {
                        Log.d("ExercisesViewModel", "Error: ${result.exception.message}")
                    }
                }
            }
        }
    }

    fun getExercisesByName(name: String){
        viewModelScope.launch(Dispatchers.IO) {
            getExercisesByNameUseCase.execute(name).let { result ->
                when (result) {
                    is Result.Success -> {
                        _exercises.value = result.data
                    }
                    is Result.Error -> {
                        Log.d("ExercisesViewModel", "Error: ${result.exception.message}")
                    }
                }
            }
        }
    }
}