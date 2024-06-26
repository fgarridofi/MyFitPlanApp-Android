package es.upsa.myfitplan.presentation.screens.exercises

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectedExercisesViewModel @Inject constructor()  : ViewModel()
{
    private val _selectedExercises : MutableStateFlow<List<MyFitPlanExercise>> = MutableStateFlow(emptyList()) //puede ser que siempre sea vacio
    val selectedExercises = _selectedExercises.asStateFlow()

    fun setSelectedExercises(exercises: List<MyFitPlanExercise>) {
        Log.d("SelectedExercisesViewModel", "Setting selected exercises: ${exercises.size}")
        exercises.forEach { exercise ->
            Log.d("SelectedExercisesViewModel", "Exercise: ${exercise.name}")
        }
        _selectedExercises.value = exercises

        Log.d("COMPROBACION", "" + _selectedExercises.value.size)
        Log.d("COMPROBACION", "" + selectedExercises.value.size)

    }

    init {
        Log.d("ViewModel", "ViewModel instantiated")

    }
}