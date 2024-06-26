package es.upsa.myfitplan.presentation.screens.exercises

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import androidx.compose.material3.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(exerciseId: String?, navController: NavController, viewModel: ExercisesViewModel = hiltViewModel()) {
    val exerciseDetails by viewModel.exerciseDetails.collectAsState()

    viewModel.getExerciseById(exerciseId ?: "")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exercises") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary

                )
            )
        }
    ) {paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            exerciseDetails?.let { exercise ->
                Box(modifier = Modifier.fillMaxSize().background(color = Color.White)) {


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
                        contentDescription = "Exercise GIF",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.FillHeight
                    )

                    Box(
                        modifier = Modifier
                            .offset(y = (16).dp)
                            .padding(top = 230.dp)
                            .fillMaxSize()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(16.dp)
                            )


                    ) {
                        LazyColumn(modifier = Modifier.padding(bottom = 70.dp)) {
                            item {
                                HeaderSection(exercise)

                                Divider(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.onSurface, thickness = 3.dp)


                                Text(text = "Instructions", style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W400,
                                    color = MaterialTheme.colorScheme.onSurface
                                ), modifier = Modifier.padding(horizontal = 20.dp, vertical = 3.dp))

                                Text(
                                    text = buildString {
                                        (exercise.instructions as List<String>).forEachIndexed { index, instruction ->
                                            append("${index + 1}. $instruction\n")
                                        }
                                    },
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W400,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                                )


                                Text(text = "Secondary muscles", style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W400,
                                    color = MaterialTheme.colorScheme.onSurface
                                ), modifier = Modifier.padding(horizontal = 20.dp, vertical = 3.dp))

                                Text(
                                    text = if (exercise.secondaryMuscles is List<*>) {
                                        (exercise.secondaryMuscles as List<String>).joinToString(", ")
                                    } else {
                                        exercise.secondaryMuscles.toString()
                                    },
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W400,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                )


                            }
                        }
                    }
                }
            } ?: Text("Loading exercise details...")
        }
    }
}


@Composable
fun ImageComponent(imageUrl: String, imageLoader: ImageLoader, contentDescription: String, modifier: Modifier, contentScale: ContentScale) {
    Image(
        painter = rememberAsyncImagePainter(model = imageUrl, imageLoader = imageLoader),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}



@Composable
fun HeaderSection(exercise : MyFitPlanExercise) {

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = exercise.name, style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.W600,
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Body part: ${exercise.bodyPart}", style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W400,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)
            )
        }


        Text(
            text = "Target: ${exercise.target}",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )


    }
}



