package es.upsa.myfitplan.data.model.exercises

import com.google.gson.annotations.SerializedName

data class Exercise(
    @SerializedName("bodyPart")
    val bodyPart: String,
    @SerializedName("equipment")
    val equipment: String,
    @SerializedName("gifUrl")
    val gifUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("target")
    val target: String,
    @SerializedName("secondaryMuscles")
    val secondaryMuscles: List<String>,
    @SerializedName("instructions")
    val instructions: List<String>

)
