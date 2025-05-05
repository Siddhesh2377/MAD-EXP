package com.dark.mad_exp.exp7

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.dark.mad_exp.ui.theme.MADEXPTheme

class TelephoneActivity : ComponentActivity() {
    private var incomingCallReceiver: IncomingCallReceiver? = null
    private var incomingNumber = mutableStateOf("")
    private var callState = mutableStateOf("")

    inner class IncomingCallReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
                val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                
                when (state) {
                    TelephonyManager.EXTRA_STATE_RINGING -> {
                        // Phone is ringing, incoming call
                        incomingNumber.value = number ?: "Unknown Number"
                        callState.value = "Incoming Call"
                        Toast.makeText(
                            context,
                            "Incoming call from: ${incomingNumber.value}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                        // Call answered or outgoing call
                        callState.value = "Call in Progress"
                    }
                    TelephonyManager.EXTRA_STATE_IDLE -> {
                        // Call ended or rejected
                        if (callState.value.isNotEmpty()) {
                            callState.value = "Call Ended"
                            // Reset after a delay
                            Handler(Looper.getMainLooper()).postDelayed({
                                callState.value = ""
                                incomingNumber.value = ""
                            }, 3000)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Register broadcast receiver for incoming calls
        incomingCallReceiver = IncomingCallReceiver()
        val intentFilter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        registerReceiver(incomingCallReceiver, intentFilter)
        
        // Request permissions
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE
            ),
            100
        )
        
        // Set up the UI
        setContent {
            TelephoneApp(this, incomingNumber, callState)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Unregister receiver
        incomingCallReceiver?.let {
            try {
                unregisterReceiver(it)
            } catch (e: Exception) {
                // Receiver might not be registered
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelephoneApp(
    activity: ComponentActivity, 
    incomingNumber: State<String>,
    callState: State<String>
) {
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    var callStatus by remember { mutableStateOf("") }
    
    // For requesting multiple permissions
    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        
        if (allGranted && phoneNumber.isNotBlank()) {
            makeCall(context, phoneNumber)
            callStatus = "Calling $phoneNumber..."
        } else {
            Toast.makeText(context, "Permissions required to make calls", Toast.LENGTH_SHORT).show()
            callStatus = "Permissions denied"
        }
    }

    MADEXPTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Phone Call App") }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Make & Receive Calls",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Show incoming call UI if there's an incoming call
                if (callState.value.isNotEmpty() && incomingNumber.value.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = callState.value,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Number: ${incomingNumber.value}",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Enter Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (phoneNumber.isNotBlank()) {
                            // Request permissions
                            val permissionsToRequest = arrayOf(
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_PHONE_STATE
                            )

                            val allGranted = permissionsToRequest.all {
                                ContextCompat.checkSelfPermission(context, it) ==
                                        PackageManager.PERMISSION_GRANTED
                            }

                            if (allGranted) {
                                makeCall(context, phoneNumber)
                                callStatus = "Calling $phoneNumber..."
                            } else {
                                val permissionsToRequestList = permissionsToRequest.toList()
                                multiplePermissionsLauncher.launch(permissionsToRequestList.toTypedArray())
                            }
                        } else {
                            Toast.makeText(context, "Please enter a phone number", Toast.LENGTH_SHORT)
                                .show()
                            callStatus = "No phone number entered"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Call Now")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (callStatus.isNotEmpty()) {
                    Text(
                        text = callStatus,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Call Status",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("The app is listening for incoming calls.")
                        Text("When a call comes in, you'll see a notification.")
                    }
                }
            }
        }
    }
}

fun makeCall(context: Context, phoneNumber: String) {
    try {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(callIntent)
    } catch (e: SecurityException) {
        Toast.makeText(context, "Permission denied to make calls", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error making call: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}