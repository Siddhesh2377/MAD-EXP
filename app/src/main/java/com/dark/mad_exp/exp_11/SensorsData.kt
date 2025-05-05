package com.dark.mad_exp.exp_11


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dark.mad_exp.ui.theme.MADEXPTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorDataScreen() {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    var accelerometerData by remember { mutableStateOf("Waiting...") }
    var gyroscopeData by remember { mutableStateOf("Waiting...") }
    var orientationData by remember { mutableStateOf("Waiting...") }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        accelerometerData =
                            "X: ${event.values[0]} Y: ${event.values[1]} Z: ${event.values[2]}"
                    }

                    Sensor.TYPE_GYROSCOPE -> {
                        gyroscopeData =
                            "X: ${event.values[0]} Y: ${event.values[1]} Z: ${event.values[2]}"
                    }

                    Sensor.TYPE_ROTATION_VECTOR -> {
                        val rotationMatrix = FloatArray(9)
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                        val orientations = FloatArray(3)
                        SensorManager.getOrientation(rotationMatrix, orientations)

                        orientationData =
                            "Azimuth: ${Math.toDegrees(orientations[0].toDouble())} " +
                                    "Pitch: ${Math.toDegrees(orientations[1].toDouble())} " +
                                    "Roll: ${Math.toDegrees(orientations[2].toDouble())}"
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.also {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    MADEXPTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Sensor Data") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {

                Text(text = "Accelerometer", style = MaterialTheme.typography.titleMedium)
                Text(text = accelerometerData)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Gyroscope", style = MaterialTheme.typography.titleMedium)
                Text(text = gyroscopeData)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Orientation", style = MaterialTheme.typography.titleMedium)
                Text(text = orientationData)
            }
        }
    }

}