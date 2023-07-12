package com.example.media3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.media3.data.VideoItem

class MainViewModel : ViewModel() {

    val videoList = mutableListOf<VideoItem>()

    init {
        /**
         * Test urls are from https://ottverse.com/free-mpeg-dash-mpd-manifest-example-test-urls/
         */
        videoList.add(VideoItem("Tears", "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd"))
        videoList.add(VideoItem("Low Latency Chunked Single-Bitrate, AVC, and AAC", "https://livesim.dashif.org/livesim/chunkdur_1/ato_7/testpic4_8s/Manifest.mpd"))
        videoList.add(VideoItem("10-bit UHD SDR Live", "https://dash.akamaized.net/dash264/TestCasesUHD/2b/11/MultiRate.mpd"))
        videoList.add(VideoItem("Switching across adaptation set – With switching element", "https://dash.akamaized.net/dash264/TestCasesIOP33/adapatationSetSwitching/5/manifest.mpd"))
        videoList.add(VideoItem("AVC, Multi-Resolution Multi-Rate, Live profile, SegmentTimeline", "https://dash.akamaized.net/dash264/TestCases/2c/qualcomm/1/MultiResMPEG2.mpd"))
        videoList.add(VideoItem("AVC, Multi-Resolution Multi-Rate, Live profile, up to 1080p", "https://dash.akamaized.net/dash264/TestCasesHD/2b/qualcomm/1/MultiResMPEG2.mpd"))
        videoList.add(VideoItem("AVC, Single-Resolution Multi-Rate, Live profile", "https://dash.akamaized.net/dash264/TestCases/1b/qualcomm/1/MultiRatePatched.mpd"))
    }
}