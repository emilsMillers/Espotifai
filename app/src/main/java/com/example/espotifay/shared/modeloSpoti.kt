package com.example.espotifay.shared

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.annotation.AnyRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.espotifay.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class Cancion(
    val title: String,
    val imageResId: Int,
    val rawResId: Int
)

class ExoPlayerViewModel : ViewModel() {
    private val _exoPlayer: MutableStateFlow<ExoPlayer?> = MutableStateFlow(null)
    val exoPlayer = _exoPlayer.asStateFlow()

    private val listaCanciones = listOf(
        Cancion("BLANCO - FRANLUCAS", R.drawable.blanco, R.raw.blanco),
        Cancion("KILL YOU - EMINEM", R.drawable.kilyu, R.raw.killyu),
        Cancion("TOOK HER TO THE O - KING VON", R.drawable.tookher, R.raw.kingvon),
        Cancion("ALREADY RICH - YEAT", R.drawable.arleady, R.raw.yeat),
        Cancion("OPPA STOPPA - YBN NAHMIR", R.drawable.pene, R.raw.oppastoppa),
        Cancion("REEL IT IN - AMINÉ", R.drawable.reel, R.raw.reel),
        Cancion("PSA - KAY FLOCK", R.drawable.psa, R.raw.psa)
    )
    private val _actual = MutableStateFlow(listaCanciones[0])
    val actual = _actual.asStateFlow()

    private val _duracion = MutableStateFlow(0)
    val duracion = _duracion.asStateFlow()

    private val _progreso = MutableStateFlow(0)
    val progreso = _progreso.asStateFlow()

    private val _repetir = MutableStateFlow(false)
    val repetir = _repetir.asStateFlow()
    private val _random = MutableStateFlow(false)
    val random = _random.asStateFlow()

    private var currentSongIndex = 0

    fun crearExoPlayer(context: Context) {
        _exoPlayer.value = ExoPlayer.Builder(context).build()
        _exoPlayer.value!!.prepare()
        _exoPlayer.value!!.playWhenReady = true
    }

    fun hacerSonarMusica(context: Context) {
        val cancion = MediaItem.fromUri(obtenerRuta(context, _actual.value.rawResId))
        _exoPlayer.value!!.setMediaItem(cancion)
        _exoPlayer.value!!.playWhenReady = true

        _exoPlayer.value!!.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _duracion.value = _exoPlayer.value!!.duration.toInt()

                    viewModelScope.launch {
                        while (isActive) {
                            _progreso.value = _exoPlayer.value!!.currentPosition.toInt()
                            delay(1000)
                        }
                    }
                } else if (playbackState == Player.STATE_ENDED) {
                    CambiarCancion(context)
                }
            }
        })
    }

    override fun onCleared() {
        _exoPlayer.value!!.release()
        super.onCleared()
    }

    fun PausarOSeguirMusica() {
        if (_exoPlayer.value!!.isPlaying) {
            _exoPlayer.value!!.pause()
        } else {
            _exoPlayer.value!!.play()
        }
    }

    fun CambiarCancion(context: Context) {
        _exoPlayer.value?.stop()
        _exoPlayer.value?.clearMediaItems()

        if (repetir.value) {
            _exoPlayer.value?.setMediaItem(MediaItem.fromUri(obtenerRuta(context, _actual.value.rawResId)))
            _exoPlayer.value?.prepare()
            _exoPlayer.value?.playWhenReady = true
        } else {
            if (_random.value) {
                var randomIndex: Int
                do {
                    randomIndex = (listaCanciones.indices - currentSongIndex).random()
                } while (randomIndex == currentSongIndex)

                currentSongIndex = randomIndex
            } else {
                currentSongIndex = (currentSongIndex + 1) % listaCanciones.size
            }

            _actual.value = listaCanciones[currentSongIndex]

            _exoPlayer.value?.setMediaItem(MediaItem.fromUri(obtenerRuta(context, _actual.value.rawResId)))
            _exoPlayer.value?.prepare()
            _exoPlayer.value?.playWhenReady = true

            if (!repetir.value && currentSongIndex == listaCanciones.size - 1) {
                currentSongIndex = 0
            }
        }
    }

    fun toglearRepetir() {
        _repetir.value = !_repetir.value
    }

    fun toglearRandom() {
        _random.value = !_random.value
    }

    fun retrocederCancion(context: Context) {
        _exoPlayer.value!!.stop()
        _exoPlayer.value!!.clearMediaItems()

        currentSongIndex = (currentSongIndex - 1 + listaCanciones.size) % listaCanciones.size
        _actual.value = listaCanciones[currentSongIndex]

        _exoPlayer.value!!.setMediaItem(MediaItem.fromUri(obtenerRuta(context, _actual.value.rawResId)))
        _exoPlayer.value!!.prepare()
        _exoPlayer.value!!.playWhenReady = true
    }

    fun irAPosicion(nuevaPosicion: Int) {
        _exoPlayer.value?.seekTo(nuevaPosicion.toLong())
    }

}

@Throws(Resources.NotFoundException::class)
fun obtenerRuta(context: Context, @AnyRes resId: Int): Uri {
    val res: Resources = context.resources
    return Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId)
    )
}
