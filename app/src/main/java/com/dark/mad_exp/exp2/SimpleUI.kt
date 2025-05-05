package com.dark.mad_exp.exp2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dark.mad_exp.ui.theme.MADEXPTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleUI() {
    MADEXPTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Simple UI with Compose") }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                var text by remember { mutableStateOf("") }

                Text(
                    text = "Welcome to Jetpack Compose\nwith Material 3",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter something") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { /* Do something */ }) {
                    Text("Click Me")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Items:", fontSize = 20.sp)

                LazyColumn {
                    items(listOf("Item 1", "Item 2", "Item 3", "Item 4")) { item ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = item,
                                modifier = Modifier.padding(16.dp),
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}