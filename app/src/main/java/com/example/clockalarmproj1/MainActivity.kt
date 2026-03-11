package com.example.clockalarmproj1

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
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

        val container = findViewById<FrameLayout>(R.id.container)
        val view = layoutInflater.inflate(R.layout.clock_layout, container, false)

        val clockText = view.findViewById<TextView>(R.id.clockText)

        container.removeAllViews()

        container.addView(view)


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

        val button = view.findViewById<Button>(R.id.setAlarmBtn)

        button.setOnClickListener {

            Handler(Looper.getMainLooper()).postDelayed({

                Toast.makeText(
                    this,
                    "Alarm Triggered!",
                    Toast.LENGTH_LONG
                ).show()

            }, 10000)

        }
    }    fun showTimer() {

        val container = findViewById<FrameLayout>(R.id.container)

        val view = layoutInflater.inflate(R.layout.timer_layout, container, false)

        container.removeAllViews()
        container.addView(view)

        val input = view.findViewById<EditText>(R.id.timerInput)
        val timerText = view.findViewById<TextView>(R.id.timerText)
        val startBtn = view.findViewById<Button>(R.id.startTimerBtn)

        startBtn.setOnClickListener {

            val seconds = input.editableText.toString().toIntOrNull() ?: 0

            object : CountDownTimer(seconds * 1000L, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    timerText.text = (millisUntilFinished / 1000).toString()
                }

                override fun onFinish() {
                    timerText.text = getString(android.R.string.ok)
                }

            }.start()
        }
    }
    fun showStopwatch() {

        val view = layoutInflater.inflate(R.layout.stopwatch_layout, null)

        val container = findViewById<FrameLayout>(R.id.container)

        container.removeAllViews()
        container.addView(view)

        val text = view.findViewById<TextView>(R.id.stopwatchText)
        val button = view.findViewById<Button>(R.id.startStopwatchBtn)

        val handler = Handler(Looper.getMainLooper())

        var startTime = 0L
        var running = false

        val runnable = object : Runnable {
            override fun run() {

                if (running) {
                    val elapsed = (SystemClock.elapsedRealtime() - startTime) / 1000
                    text.text = elapsed.toString()
                    handler.postDelayed(this, 1000)
                }

            }
        }

        button.setOnClickListener {

            if (!running) {

                startTime = SystemClock.elapsedRealtime()
                running = true
                handler.post(runnable)

                button.text = "Stop"

            } else {

                running = false
                button.text = "Start"

            }

        }
    }
}