package es.upsa.myfitplan.domain.repository

import es.upsa.myfitplan.domain.model.routine.Routine

interface LocalRepository {
    fun saveRoutine(routine: Routine)
    fun getRoutines(): List<Routine>
    fun deleteRoutine(routineId: Int)
    fun getRoutineById(routineId: Int): Routine
    fun updateRoutine(routine: Routine)
}