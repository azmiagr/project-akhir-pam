package com.example.project_akhir_pam.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.math.sqrt

class ShakeSensorImpl(context: Context) : ShakeSensor, SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val shakeChannel = Channel<Unit>(Channel.BUFFERED)
    override val onShake: Flow<Unit> = shakeChannel.receiveAsFlow()

    private val shakeThreshold = 12f
    private var lastTime = 0L

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

        val now = System.currentTimeMillis()
        if (acceleration > shakeThreshold && now - lastTime > 1200) {
            lastTime = now
            shakeChannel.trySend(Unit)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun stop() {
        sensorManager.unregisterListener(this)
    }
}