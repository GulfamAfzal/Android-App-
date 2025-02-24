package com.example.second

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var musicUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val pickFileButton: Button = findViewById(R.id.pickFileButton)
        val playButton: Button = findViewById(R.id.playButton)
        val pauseButton: Button = findViewById(R.id.pauseButton)
        val stopButton: Button = findViewById(R.id.stopButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)
        val titleText: TextView = findViewById(R.id.titleText)

        // Animate Title (Music Studio)
        titleText.animate()
            .alpha(1f)
            .setDuration(1000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // File picker intent
        val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    musicUri = uri
                    Toast.makeText(this, "🎵 File Selected!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Pick file button click
        pickFileButton.setOnClickListener {
            animateButton(it as Button)
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "audio/*"
            }
            filePickerLauncher.launch(intent)
        }

        // Play music
        playButton.setOnClickListener {
            animateButton(it as Button)
            musicUri?.let {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(this@MainActivity, it)
                        prepare()
                        start()
                    }
                    Toast.makeText(this, "▶ Playing Music", Toast.LENGTH_SHORT).show()
                } else {
                    mediaPlayer?.start()
                }
            }
        }

        // Pause music
        pauseButton.setOnClickListener {
            animateButton(it as Button)
            mediaPlayer?.pause()
            Toast.makeText(this, "⏸ Music Paused", Toast.LENGTH_SHORT).show()
        }

        // Stop music
        stopButton.setOnClickListener {
            animateButton(it as Button)
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            Toast.makeText(this, "⏹ Music Stopped", Toast.LENGTH_SHORT).show()
        }

        // Cancel file selection
        cancelButton.setOnClickListener {
            animateButton(it as Button)
            musicUri = null
            mediaPlayer?.release()
            mediaPlayer = null
            Toast.makeText(this, "❌ File Selection Canceled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animateButton(button: Button) {
        button.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                button.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }
            .start()
    }
}
