package es.upsa.myfitplan.data.mapper

import es.upsa.myfitplan.data.model.exercises.Exercise
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise

object ExerciseMapper {


     fun mapExerciseToDomain(exercise: Exercise): MyFitPlanExercise {
        return MyFitPlanExercise(
            bodyPart = exercise.bodyPart,
            equipment = exercise.equipment,
            gifUrl = exercise.gifUrl,
            id = exercise.id,
            name = exercise.name,
            target = exercise.target,
            secondaryMuscles = exercise.secondaryMuscles,
            instructions = exercise.instructions
        )
    }

    fun mapListExerciseToDomain(exercises: List<Exercise>): List<MyFitPlanExercise> {
        return exercises.map {
            mapExerciseToDomain(it)
        }
    }

}