package com.example.astrobeat

import android.media.MediaPlayer

object appState {
    var playing: Boolean = false
    var paused: Boolean = false
    var currentMusic: MediaPlayer? =  null
}