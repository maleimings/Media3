package com.example.media3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerView
import com.example.media3.data.VideoItem
import com.example.media3.ui.theme.Media3Theme
import com.example.media3.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by inject<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Media3Theme {
                Column {
                    mainViewModel.videoList.first().let {
                        VideoView(videoUrl = it.url)
                    }
                    VideoList(videoList = mainViewModel.videoList)
                }
            }
        }
    }
}

@Composable
fun VideoList(videoList: List<VideoItem>) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp)) {
        items(videoList) {
            VideoItemView(it)
        }
    }
}

@Composable
fun VideoItemView(item: VideoItem) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
            Text(text = item.url, style = MaterialTheme.typography.bodyMedium)
        }
    }

}

@Composable
fun VideoView(videoUrl: String) {
    val context = LocalContext.current

    val player = ExoPlayer.Builder(context).build()
        .also { exoPlayer ->
            val mediaItem = MediaItem.fromUri(videoUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }

    val lifeCycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(
        AndroidView(modifier = Modifier.focusable(), factory = {
            PlayerView(context).apply {
                this.player = player
            }
        })
    ) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    player.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    player.play()
                }

                else -> {}
            }
        }

        val lifecycle = lifeCycleOwner.value.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            player.release()
            lifecycle.removeObserver(observer)
        }
    }
}