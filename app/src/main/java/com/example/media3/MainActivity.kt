package com.example.media3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "ts", Toast.LENGTH_LONG).show()
    }
}