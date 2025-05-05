package com.dark.mad_exp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dark.mad_exp.exp3.AnimatedBox
import com.dark.mad_exp.exp8.CameraSnapshotApp
import com.dark.mad_exp.exp_11.SensorDataScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            //SimpleUI()
            AnimatedBox()
            //RegisterLoginScreen()
            //CollegeApp()
            //ContactListApp()
            //CameraSnapshotApp()
            SensorDataScreen()
        }
    }
}



