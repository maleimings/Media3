package com.example.media3.data

import androidx.media3.common.MimeTypes

data class VideoItem(
    val title: String,
    val url: String,
    val mediaType: String = MimeTypes.APPLICATION_MPD,
    val drmUrl: String = "",
)

