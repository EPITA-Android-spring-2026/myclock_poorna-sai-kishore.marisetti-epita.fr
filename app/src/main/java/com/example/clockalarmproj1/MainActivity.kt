package com.example.clockalarmproj1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnClock = findViewById<Button>(R.id.btnClock)
        val btnAlarm = findViewById<Button>(R.id.btnAlarm)
        val btnTimer = findViewById<Button>(R.id.btnTimer)
        val btnStopwatch = findViewById<Button>(R.id.btnStopwatch)

        btnClock.setOnClickListener {
            showClock()
        }

        btnAlarm.setOnClickListener {
            showAlarm()
        }

        btnTimer.setOnClickListener {
            showTimer()
        }

        btnStopwatch.setOnClickListener {
            showStopwatch()
        }
        showClock()
    }

    fun showClock() {

        val view = layoutInflater.inflate(R.layout.clock_layout, null)

        val container = findViewById<FrameLayout>(R.id.container)

        container.removeAllViews()

        container.addView(view)

        val clockText = view.findViewById<TextView>(R.id.clockText)

        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {

                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val currentTime = sdf.format(Date())

                clockText.text = currentTime

                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)
    }
    fun showAlarm() {

        val view = layoutInflater.inflate(R.layout.alarm_layout, null)

        val container = findViewById<FrameLayout>(R.id.container)

        container.removeAllViews()

        container.addView(view)
    }
    fun showTimer() {
        val view = layoutInflater.inflate(R.layout.timer_layout, null)

        val container = findViewById<FrameLayout>(R.id.container)

        container.removeAllViews()

        container.addView(view)
    }
    fun showStopwatch() {
        val view = layoutInflater.inflate(R.layout.stopwatch_layout, null)

        val container = findViewById<FrameLayout>(R.id.container)

        container.removeAllViews()

        container.addView(view)
    }

}