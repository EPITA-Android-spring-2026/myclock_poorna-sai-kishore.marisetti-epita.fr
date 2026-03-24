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
import android.widget.ToggleButton
import android.app.Activity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        val hoursText = view.findViewById<TextView>(R.id.hoursText)
        val minutesText = view.findViewById<TextView>(R.id.minutesText)
        val secondsText = view.findViewById<TextView>(R.id.secondsText)
        val ampmText = view.findViewById<TextView>(R.id.ampmText)
        val dateText = view.findViewById<TextView>(R.id.dateText)

        container.removeAllViews()
        container.addView(view)

        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                val calendar = java.util.Calendar.getInstance()
                val hours = calendar.get(java.util.Calendar.HOUR)
                val minutes = calendar.get(java.util.Calendar.MINUTE)
                val seconds = calendar.get(java.util.Calendar.SECOND)
                val ampm = if (calendar.get(java.util.Calendar.AM_PM) == java.util.Calendar.AM) "AM" else "PM"

                hoursText.text = String.format("%02d", hours)
                minutesText.text = String.format("%02d", minutes)
                secondsText.text = String.format("%02d", seconds)
                ampmText.text = ampm

                val sdf = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
                dateText.text = sdf.format(Date())

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

        val alarmHoursInput = view.findViewById<EditText>(R.id.alarmHoursInput)
        val alarmMinutesInput = view.findViewById<EditText>(R.id.alarmMinutesInput)
        val alarmAmPmToggle = view.findViewById<ToggleButton>(R.id.alarmAmPmToggle)
        val alarmDisplayText = view.findViewById<TextView>(R.id.alarmDisplayText)
        val alarmStatusText = view.findViewById<TextView>(R.id.alarmStatusText)
        val alarmCountdownText = view.findViewById<TextView>(R.id.alarmCountdownText)
        val setAlarmBtn = view.findViewById<Button>(R.id.setAlarmBtn)
        val cancelAlarmBtn = view.findViewById<Button>(R.id.cancelAlarmBtn)
        val dismissAlarmBtn = view.findViewById<Button>(R.id.dismissAlarmBtn)

        var alarmTimer: CountDownTimer? = null
        var alarmSet = false

        fun updateAlarmDisplay() {
            val hours = alarmHoursInput.text.toString().padStart(2, '0')
            val minutes = alarmMinutesInput.text.toString().padStart(2, '0')
            val ampm = if (alarmAmPmToggle.isChecked) "PM" else "AM"
            alarmDisplayText.text = "$hours:$minutes $ampm"
        }

        setAlarmBtn.setOnClickListener {
            val hours = alarmHoursInput.text.toString().toIntOrNull() ?: 0
            val minutes = alarmMinutesInput.text.toString().toIntOrNull() ?: 0

            if (hours < 0 || hours > 12 || minutes < 0 || minutes > 59) {
                Toast.makeText(this, "Invalid time entered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (hours == 0 && minutes == 0) {
                Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            updateAlarmDisplay()
            alarmSet = true
            alarmStatusText.text = "Alarm is ON"
            setAlarmBtn.isEnabled = false
            cancelAlarmBtn.isEnabled = true

            val totalMillis = (hours * 3600000L) + (minutes * 60000L)

            alarmTimer?.cancel()
            alarmTimer = object : CountDownTimer(totalMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val remainingHours = (millisUntilFinished / 3600000)
                    val remainingMinutes = (millisUntilFinished % 3600000) / 60000
                    val remainingSeconds = (millisUntilFinished % 60000) / 1000

                    alarmCountdownText.text = String.format(
                        "Time until alarm: %02d:%02d:%02d",
                        remainingHours,
                        remainingMinutes,
                        remainingSeconds
                    )
                }

                override fun onFinish() {
                    Toast.makeText(
                        this@MainActivity,
                        "Alarm Triggered! ${alarmDisplayText.text}",
                        Toast.LENGTH_LONG
                    ).show()
                    alarmStatusText.text = "Alarm TRIGGERED!"
                    alarmCountdownText.text = ""
                    dismissAlarmBtn.visibility = android.view.View.VISIBLE
                }
            }.start()
        }

        cancelAlarmBtn.setOnClickListener {
            alarmTimer?.cancel()
            alarmSet = false
            alarmStatusText.text = "Alarm is OFF"
            alarmCountdownText.text = ""
            alarmDisplayText.text = "--:-- AM"
            setAlarmBtn.isEnabled = true
            cancelAlarmBtn.isEnabled = false
            dismissAlarmBtn.visibility = android.view.View.GONE
            Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show()
        }

        dismissAlarmBtn.setOnClickListener {
            alarmTimer?.cancel()
            alarmSet = false
            alarmStatusText.text = "Alarm is OFF"
            alarmCountdownText.text = ""
            alarmDisplayText.text = "--:-- AM"
            setAlarmBtn.isEnabled = true
            cancelAlarmBtn.isEnabled = false
            dismissAlarmBtn.visibility = android.view.View.GONE
        }
    }

    fun showTimer() {
        val container = findViewById<FrameLayout>(R.id.container)
        val view = layoutInflater.inflate(R.layout.timer_layout, container, false)

        container.removeAllViews()
        container.addView(view)

        val timerMinutesInput = view.findViewById<EditText>(R.id.timerMinutesInput)
        val timerSecondsInput = view.findViewById<EditText>(R.id.timerSecondsInput)
        val timerMinutesText = view.findViewById<TextView>(R.id.timerMinutesText)
        val timerSecondsText = view.findViewById<TextView>(R.id.timerSecondsText)
        val timerMillisecondsText = view.findViewById<TextView>(R.id.timerMillisecondsText)
        val startBtn = view.findViewById<Button>(R.id.startTimerBtn)
        val pauseBtn = view.findViewById<Button>(R.id.stopTimerBtn)
        val resetBtn = view.findViewById<Button>(R.id.resetTimerBtn)

        var countDownTimer: CountDownTimer? = null
        var timerRunning = false

        fun resetDisplay() {
            timerMinutesText.text = "00"
            timerSecondsText.text = "00"
            timerMillisecondsText.text = "00"
        }

        startBtn.setOnClickListener {
            if (!timerRunning) {
                val minutes = timerMinutesInput.text.toString().toIntOrNull() ?: 0
                val seconds = timerSecondsInput.text.toString().toIntOrNull() ?: 0

                if (minutes == 0 && seconds == 0) {
                    Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val totalMillis = (minutes * 60000L) + (seconds * 1000L)

                countDownTimer?.cancel()
                countDownTimer = object : CountDownTimer(totalMillis, 10) {
                    override fun onTick(millisUntilFinished: Long) {
                        val mins = (millisUntilFinished / 60000) % 60
                        val secs = (millisUntilFinished / 1000) % 60
                        val millis = (millisUntilFinished % 1000) / 10

                        timerMinutesText.text = String.format("%02d", mins)
                        timerSecondsText.text = String.format("%02d", secs)
                        timerMillisecondsText.text = String.format("%02d", millis)
                    }

                    override fun onFinish() {
                        timerMinutesText.text = "00"
                        timerSecondsText.text = "00"
                        timerMillisecondsText.text = "00"
                        Toast.makeText(this@MainActivity, "Timer Finished!", Toast.LENGTH_LONG).show()
                        timerRunning = false
                        startBtn.isEnabled = true
                        pauseBtn.isEnabled = false
                        resetBtn.isEnabled = true
                    }
                }.start()

                timerRunning = true
                startBtn.isEnabled = false
                pauseBtn.isEnabled = true
                resetBtn.isEnabled = false
                timerMinutesInput.isEnabled = false
                timerSecondsInput.isEnabled = false
            }
        }

        pauseBtn.setOnClickListener {
            countDownTimer?.cancel()
            timerRunning = false
            startBtn.isEnabled = true
            pauseBtn.isEnabled = false
            resetBtn.isEnabled = true
            timerMinutesInput.isEnabled = true
            timerSecondsInput.isEnabled = true
        }

        resetBtn.setOnClickListener {
            countDownTimer?.cancel()
            timerRunning = false
            resetDisplay()
            timerMinutesInput.text.clear()
            timerSecondsInput.text.clear()
            startBtn.isEnabled = true
            pauseBtn.isEnabled = false
            resetBtn.isEnabled = false
            timerMinutesInput.isEnabled = true
            timerSecondsInput.isEnabled = true
        }
    }

    fun showStopwatch() {
        val view = layoutInflater.inflate(R.layout.stopwatch_layout, null)
        val container = findViewById<FrameLayout>(R.id.container)

        container.removeAllViews()
        container.addView(view)

        val minutesText = view.findViewById<TextView>(R.id.minutesText)
        val secondsText = view.findViewById<TextView>(R.id.secondsText)
        val millisecondsText = view.findViewById<TextView>(R.id.millisecondsText)
        val startBtn = view.findViewById<Button>(R.id.startStopwatchBtn)
        val stopBtn = view.findViewById<Button>(R.id.stopStopwatchBtn)
        val resetBtn = view.findViewById<Button>(R.id.resetStopwatchBtn)

        val handler = Handler(Looper.getMainLooper())

        var startTime = 0L
        var pausedTime = 0L
        var running = false

        val runnable = object : Runnable {
            override fun run() {
                if (running) {
                    val elapsed = SystemClock.elapsedRealtime() - startTime + pausedTime

                    val totalMillis = elapsed % 1000
                    val totalSeconds = (elapsed / 1000) % 60
                    val totalMinutes = (elapsed / 60000) % 60

                    minutesText.text = String.format("%02d", totalMinutes)
                    secondsText.text = String.format("%02d", totalSeconds)
                    millisecondsText.text = String.format("%02d", totalMillis / 10)

                    handler.postDelayed(this, 10)
                }
            }
        }

        startBtn.setOnClickListener {
            if (!running) {
                startTime = SystemClock.elapsedRealtime()
                running = true
                startBtn.isEnabled = false
                stopBtn.isEnabled = true
                resetBtn.isEnabled = false
                handler.post(runnable)
            }
        }

        stopBtn.setOnClickListener {
            if (running) {
                running = false
                pausedTime += SystemClock.elapsedRealtime() - startTime
                startBtn.isEnabled = true
                stopBtn.isEnabled = false
                resetBtn.isEnabled = true
            }
        }

        resetBtn.setOnClickListener {
            running = false
            pausedTime = 0L
            startTime = 0L
            minutesText.text = "00"
            secondsText.text = "00"
            millisecondsText.text = "00"
            startBtn.isEnabled = true
            stopBtn.isEnabled = false
            resetBtn.isEnabled = false
            handler.removeCallbacks(runnable)
        }
    }
}