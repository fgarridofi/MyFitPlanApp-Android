package es.upsa.myfitplan.data.mapper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromExerciseList(exercises: List<MyFitPlanExercise>?): String {
        if (exercises == null) {
            return "[]"
        }
        val type = object : TypeToken<List<MyFitPlanExercise>>() {}.type
        return gson.toJson(exercises, type)
    }

    @TypeConverter
    fun toExerciseList(exerciseString: String): List<MyFitPlanExercise>? {
        if (exerciseString == "[]") {
            return emptyList()
        }
        val type = object : TypeToken<List<MyFitPlanExercise>>() {}.type
        return gson.fromJson(exerciseString, type)
    }
}