package com.example.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.sensor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sensorButton.text = if (!viewModel.doListen.value!!) {
            "Start"
        } else {
            "Stop"
        }

        binding.sensorButton.setOnClickListener {
            if (viewModel.doListen.value == true) {
                sensorManager.unregisterListener(this)
                binding.sensorButton.text = "Start"
                viewModel.doListen.value = false
            } else {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
                binding.sensorButton.text = "Stop"
                viewModel.doListen.value = true
            }
        }

        viewModel.liveLinearAcceleration.observe(this) {
            binding.xValue.text = it.first().round(3).toString()
            binding.yValue.text = it[1].round(3).toString()
            binding.zValue.text = it[2].round(3).toString()
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.doListen.value!!) sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val alpha = 0.8f

        viewModel.gravity[0] = alpha * viewModel.gravity[0] + (1 - alpha) * event!!.values[0]
        viewModel.gravity[1] = alpha * viewModel.gravity[1] + (1 - alpha) * event.values[1]
        viewModel.gravity[2] = alpha * viewModel.gravity[2] + (1 - alpha) * event.values[2]

        viewModel.linearAcceleration[0] = event.values[0] - viewModel.gravity[0]
        viewModel.linearAcceleration[1] = event.values[1] - viewModel.gravity[1]
        viewModel.linearAcceleration[2] = event.values[2] - viewModel.gravity[2]

        viewModel.liveLinearAcceleration.value = viewModel.linearAcceleration
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}