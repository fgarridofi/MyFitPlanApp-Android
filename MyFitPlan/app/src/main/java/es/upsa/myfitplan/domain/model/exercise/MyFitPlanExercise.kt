package es.upsa.myfitplan.domain.model.exercise

import es.upsa.myfitplan.domain.model.series.Series


data class MyFitPlanExercise (
    val bodyPart: String,
    val equipment: String? = null,
    val gifUrl: String,
    val id: String,
    val name: String,
    val target: String,
    val secondaryMuscles: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val series: List<Series> = emptyList()
)


