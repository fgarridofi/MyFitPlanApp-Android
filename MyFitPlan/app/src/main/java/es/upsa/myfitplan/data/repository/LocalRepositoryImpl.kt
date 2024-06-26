package es.upsa.myfitplan.data.repository

import com.google.gson.Gson
import es.upsa.myfitplan.data.db.RoutinesDao
import es.upsa.myfitplan.domain.model.routine.Routine
import es.upsa.myfitplan.domain.repository.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val routinesDao: RoutinesDao
) : LocalRepository {

    private val gson: Gson = Gson()

    override fun saveRoutine(routine: Routine) {
        routinesDao.insert(routine)
    }

    override fun getRoutines(): List<Routine> {
        return routinesDao.getAll()
    }

    override fun deleteRoutine(routineId: Int) {
        routinesDao.deleteById(routineId)
    }

    override fun getRoutineById(routineId: Int): Routine {
        return routinesDao.getById(routineId)
    }

    override fun updateRoutine(routine: Routine) {
        routinesDao.update(routine)
    }
}