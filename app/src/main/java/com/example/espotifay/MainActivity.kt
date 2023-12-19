package com.example.espotifay

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.espotifay.shared.ExoPlayerViewModel
import com.example.espotifay.ui.theme.EspotifayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EspotifayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Espoti()
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Espoti() {
    val contexto = LocalContext.current
    val exoPlayerViewModel: ExoPlayerViewModel = viewModel()

    val actual by remember { exoPlayerViewModel.actual }.collectAsState()
    val progreso by exoPlayerViewModel.progreso.collectAsState()
    val duracion by exoPlayerViewModel.duracion.collectAsState()

    LaunchedEffect(Unit) {
        exoPlayerViewModel.crearExoPlayer(contexto)
        exoPlayerViewModel.hacerSonarMusica(contexto)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Text(
            text = "Now Playing",
            color = Color.White
        )
        Text(
            text = actual.title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.shadow(15.dp, shape = MaterialTheme.shapes.medium)
        )

        Image(
            painter = painterResource(id = actual.imageResId),
            contentDescription = "Fotito",
            modifier = Modifier
                .size(300.dp)
                .shadow(15.dp, shape = MaterialTheme.shapes.medium)
        )
        Slider(
            value = progreso.toFloat(),
            onValueChange = { nuevaPosicion ->
                exoPlayerViewModel.irAPosicion(nuevaPosicion.toInt())
            },
            valueRange = 0f..duracion.toFloat(),
            steps = 100
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {
            Text(
                text = formatMillis(progreso),
                color = Color.White
            )
            Text(
                text = formatMillis(duracion),
                color = Color.White
            )
        }


        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { exoPlayerViewModel.elegirCancionAleatoria(contexto) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.shadow(1.dp, shape = CircleShape)
            ) {
                Icon(Icons.Default.Shuffle, contentDescription = "Random", tint = Color.Black)
            }
            Button(
                onClick = { exoPlayerViewModel.retrocederCancion(contexto) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.shadow(1.dp, shape = CircleShape)
            ) {
                Icon(
                    Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    tint = Color.Black
                )
            }
            Button(
                onClick = { exoPlayerViewModel.PausarOSeguirMusica() },
                modifier = Modifier.shadow(15.dp, shape = MaterialTheme.shapes.medium)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Pause", tint = Color.Black)
            }
            Button(
                onClick = { exoPlayerViewModel.CambiarCancion(contexto) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.shadow(1.dp, shape = CircleShape)
            ) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.Black)
            }
            Button(
                onClick = { exoPlayerViewModel.toglearRepetir() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.shadow(1.dp, shape = CircleShape)
            ) {
                Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = if (exoPlayerViewModel.repetir.value) Color.Green else Color.Black)
            }
        }
    }
}

private fun formatMillis(millis: Int): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

