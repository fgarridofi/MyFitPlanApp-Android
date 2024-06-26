package es.upsa.myfitplan.presentation.screens.exercises

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise
import es.upsa.myfitplan.presentation.navigation.MyFitPlanScreens
import es.upsa.myfitplan.presentation.widgets.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchExerciseScreen(navController: NavController, exercisesviewModel: ExercisesViewModel = hiltViewModel()) {

    val exercises by exercisesviewModel.exercises.collectAsState(initial = emptyList())
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val isLoading by exercisesviewModel.isLoading.collectAsState()

        Column(modifier = Modifier.padding(2.dp)) {
            SearchBar(
                searchQueryState = searchQuery,
                placeHolder = "Search Exercises...",
                onSearch = { query ->
                    if (query != null && query.isNotEmpty()) {
                        exercisesviewModel.getExercisesByName(query)
                    } else {
                        exercisesviewModel.exercises
                    }
                }
            )
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
            } else {
                LazyColumn {
                    items(exercises) { exercise ->
                        ExerciseItem(exercise = exercise, navController = navController)
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
                }
            }
        }

}

@Composable
fun ExerciseItem(
    exercise: MyFitPlanExercise,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .clickable { navController.navigate("${MyFitPlanScreens.ExerciseDetailScreen.name}/${exercise.id}") },

        verticalAlignment = Alignment.CenterVertically
    ) {
        // Load the image from the URL
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory()) // Use ImageDecoder for Android P and above
                } else {
                    add(GifDecoder.Factory()) // Use GifDecoder for versions below Android P
                }
            }
            .build()

        ImageComponent(
            imageUrl = exercise.gifUrl,
            imageLoader = imageLoader,
            contentDescription = "GIF for testing"
        )

        Column(modifier = Modifier
            .padding(horizontal = 8.dp)
            .weight(1f)) {
            Text(text = exercise.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.size(4.dp))
            Text("Target: ${exercise.target}", style = MaterialTheme.typography.bodyMedium)
        }

        Button(
            onClick = { navController.navigate("${MyFitPlanScreens.ExerciseDetailScreen.name}/${exercise.id}") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)
        ) {
            Text(text = "Details", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


