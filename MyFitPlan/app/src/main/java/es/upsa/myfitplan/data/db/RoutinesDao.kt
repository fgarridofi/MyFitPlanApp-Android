package es.upsa.myfitplan.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.upsa.myfitplan.domain.model.routine.Routine

@Dao
interface RoutinesDao {

    @Query("SELECT * FROM Routine")
    fun getAll(): List<Routine>

    @Query("SELECT * FROM Routine WHERE id = :id")
    fun getById(id: Int): Routine

    @Query("DELETE FROM Routine")
    fun deleteAll()

    @Query("DELETE FROM Routine WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM Routine")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(routines: List<Routine>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(routine: Routine)

    @Update
    fun update(routine: Routine)

}