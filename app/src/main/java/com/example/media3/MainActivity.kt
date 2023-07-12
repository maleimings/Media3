package com.example.media3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.media3.data.VideoItem
import com.example.media3.ui.theme.Media3Theme
import com.example.media3.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by inject<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Media3Theme {
                VideoScreen(mainViewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun VideoScreen(mainViewModel: MainViewModel) {
    var videoUrl by remember {
        mutableStateOf(mainViewModel.videoList.first().url)
    }
    Column {

        VideoView(videoUrl = videoUrl)

        VideoList(videoList = mainViewModel.videoList) {
            videoUrl = it
        }
    }
}

@Composable
fun VideoList(videoList: List<VideoItem>, onItemClick: (url: String) -> Unit) {
    val context = LocalContext.current
    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp)) {
        items(videoList) {
            val item = it
            VideoItemView(item) {
                onItemClick(item.url)
            }
        }
    }
}

@Composable
fun VideoItemView(item: VideoItem, onClick: (url: String) -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .clickable {
            onClick(item.url)
        }) {
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

    val player = remember(context) {
        ExoPlayer.Builder(context).build()
    }

    player.stop()


    val mediaItem = MediaItem.fromUri(videoUrl)
    player.setMediaItem(mediaItem)
    player.prepare()
    player.play()

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