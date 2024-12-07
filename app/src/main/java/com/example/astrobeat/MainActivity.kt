package com.example.astrobeat

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Property
import android.view.View
import android.widget.Button
import android.widget.Switch
import com.example.astrobeat.appState.currentMusic
import com.example.astrobeat.appState.paused
import com.example.astrobeat.appState.playing
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {

    var dontSkip = false
    private var looped = true
//    private var shufflePlay = false

//    private var currentMusic: MediaPlayer? =  null
    private var playlist: Array<MediaPlayer> = arrayOf()
    private lateinit var songlist: Array<MediaPlayer>
    private lateinit var switch1: Chip
    private lateinit var switch2: Chip
    private lateinit var switch3: Chip
    private lateinit var switch4: Chip
    private lateinit var switch5: Chip
    private lateinit var switch6: Chip
    private lateinit var playbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switch1 = findViewById(R.id.switch1)
        switch2 = findViewById(R.id.switch2)
        switch3 = findViewById(R.id.switch3)
        switch4 = findViewById(R.id.switch4)
        switch5 = findViewById(R.id.switch5)
        switch6 = findViewById(R.id.switch6)
        playbtn = findViewById(R.id.play)
        songlist = arrayOf(
            MediaPlayer.create(this, R.raw.brainpower),
            MediaPlayer.create(this, R.raw.synthwavedangerzone),
            MediaPlayer.create(this, R.raw.werefinallylanding),
            MediaPlayer.create(this, R.raw.billiejeaninstrumental),
            MediaPlayer.create(this, R.raw.thelema),
            MediaPlayer.create(this, R.raw.ontherun)
        )
        val switches = arrayOf(switch1, switch2, switch3, switch4, switch5, switch6)

        playbtn.text = if (playing) "   PAUSE   " else "   PLAY   "

        switches.forEach { switch: Chip ->
            switch.setOnCheckedChangeListener { chip, checked ->
                animate(chip, View.ROTATION_Y, chip.rotationY, if(checked) 8F else -9F, 225)
                animate(chip, View.TRANSLATION_X, chip.translationX, if(checked) 10f else -5F, 225)
            }
            /*switch.setOnDragListener { view, dragEvent ->

                true
            }*/
        }
    }

    private fun animate(target: View, prprty: Property<View, Float>, from: Float, to: Float, dur: Long) {
        val animator = ObjectAnimator.ofFloat(target, prprty, from, to)
        animator.duration = dur
        animator.start()
    }

    fun playAll(view: View? = null) {
        playing = !playing
            playbtn.text = if(playing) "   PAUSE   " else "   PLAY   "
        if(!playing) {
            paused = true
            currentMusic?.pause()
        }
        if(switch1.isChecked) playlist += songlist[0]
        if(switch2.isChecked) playlist += songlist[1]
        if(switch3.isChecked) playlist += songlist[2]
        if(switch4.isChecked) playlist += songlist[3]
        if(switch5.isChecked) playlist += songlist[4]
        if(switch6.isChecked) playlist += songlist[5]

//        var songNumb = 0 //might not need *
        if(playing) {
            if(paused) {
                currentMusic?.start()
                paused = false
            }
            else playNext()
        }

    }

    private fun playNext() {

        currentMusic?.setOnCompletionListener {
            play()
        } ?: run {
            println("Can't set listener, trying again")
            dontSkip = true
            currentMusic = try {
                playlist.component1()
            } catch(e: Exception) {
                println(e)
                println("Can't set listener, make sure at least 1 song is enabled")
                null
            }
            play()
        }
    }

    private fun play() {

        if(playing) {
//                Handler().postDelayed({
            var nextSongIndx = if(currentMusic == null /*|| currentMusic == playlist[playlist.size - 1]*/) 0 else (playlist.indexOf(currentMusic)) + 1
            if(dontSkip) {
                nextSongIndx --
                dontSkip = false
            }
            if(nextSongIndx >= playlist.size) {
//                    songNumb = 0 // *songNumb ++ at end may cause problems, maybe change to -1? //*
                if(looped) {
                    nextSongIndx = 0
                } else playing = false
                //TODO: Check if looped is on, if so, set nextSongIndx to 0, start over at playlist[0], or just repeat playAll(). Otherwise, nothing happens and playlist stops playing.
            } //else {
            currentMusic = try {
                playlist[nextSongIndx]
            } catch(e: Exception) {
                println(e)
                null
            }
//                }
            if(nextSongIndx < playlist.size && playing && currentMusic != null) playMusic()
            else playing = false

//                songNumb ++ //*
            playbtn.text = if(playing) "PAUSE" else "PLAY"
            currentMusic?.setOnCompletionListener(null)
            playNext()
//                }, (currentMusic?.duration?.toLong() ?: 3L) + 15L)
        }
    }

    private fun playMusic() {

        if (currentMusic == null) {
            currentMusic!!.isLooping = false
            currentMusic!!.start()

        } else currentMusic!!.start()
    }


}