package es.upsa.myfitplan.domain.model.routine

import androidx.room.Entity
import androidx.room.PrimaryKey
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise

@Entity
data class Routine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val exercises : List<MyFitPlanExercise>
)

