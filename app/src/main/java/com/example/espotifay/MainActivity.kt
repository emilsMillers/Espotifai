package com.example.espotifay

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun Espoti() {
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
            text = "BLANCO - FRANLUCAS",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.shadow(15.dp ,shape = MaterialTheme.shapes.medium)
        )

        Image(
            painter = painterResource(id = R.drawable.blanco),
            contentDescription = "Fotito",
            modifier = Modifier
                .size(300.dp)
                .shadow(15.dp, shape = MaterialTheme.shapes.medium)
        )
        Slider(value = Float.MIN_VALUE, onValueChange = {})
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {
            Text(
                text = "00:00",
                color = Color.White
            )
            Text(
                text = "3:41",
                color = Color.White
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { /*TODO*/ },modifier = Modifier.shadow(15.dp ,shape = MaterialTheme.shapes.medium)) {
                Icon(Icons.Default.Shuffle, contentDescription = "Random", tint = Color.Black)
            }
            Button(onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), modifier = Modifier.shadow(1.dp, shape = CircleShape)) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = Color.Black )
            }
            Button(onClick = { /*TODO*/ },modifier = Modifier.shadow(15.dp ,shape = MaterialTheme.shapes.medium)) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Pause", tint = Color.Black)
            }
            Button(onClick = { /*TODO*/ },modifier = Modifier.shadow(15.dp ,shape = MaterialTheme.shapes.medium)) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.Black)
            }
            Button(onClick = { /*TODO*/ },modifier = Modifier.shadow(15.dp ,shape = MaterialTheme.shapes.medium)) {
                Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = Color.Green)
            }
        }
    }
}
