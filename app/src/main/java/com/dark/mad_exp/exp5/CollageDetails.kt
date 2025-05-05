package com.dark.mad_exp.exp5

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Call
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.MenuBook
import androidx.compose.material.icons.twotone.School
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.dark.mad_exp.R
import com.dark.mad_exp.ui.theme.MADEXPTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollegeApp() {
    MADEXPTheme {
        var drawerState = rememberDrawerState(DrawerValue.Closed)
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val menuItems = listOf("About Us", "Departments", "Student Section", "Contact Us")
        val menuIcons = listOf(
            Icons.TwoTone.Info,
            Icons.TwoTone.School,
            Icons.TwoTone.AccountCircle,
            Icons.TwoTone.Call
        )

        var selectedMenu by remember { mutableStateOf(menuItems[0]) }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("Menu", modifier = Modifier.padding(16.dp), fontSize = 20.sp)
                    menuItems.forEachIndexed { index, menu ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedMenu = menu
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                                .padding(12.dp)
                        ) {
                            Icon(
                                imageVector = menuIcons[index],
                                contentDescription = null,
                                tint = if (selectedMenu == menu) Color(0xFF0866FF) else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(menu)
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(selectedMenu) }
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    when (selectedMenu) {

                        // ABOUT US
                        "About Us" -> {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "About Us",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(120.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Government Polytechnic Mumbai is one of the premier institutions providing technical education in Mumbai.",
                                    fontSize = 18.sp
                                )
                            }
                        }

                        // DEPARTMENTS
                        "Departments" -> {
                            val departments = listOf(
                                "Computer Engineering",
                                "Information Technology",
                                "Electronics Engineering",
                                "Mechanical Engineering",
                                "Civil Engineering",
                                "Electrical Engineering",
                                "Applied Science and Humanities"
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.padding(16.dp),
                                contentPadding = PaddingValues(8.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(departments) { dept ->
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .fillMaxWidth()
                                            .clickable { /* Handle click if needed */ }
                                            .padding(16.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.align(Alignment.Center)
                                        ) {
                                            Icon(
                                                imageVector = Icons.TwoTone.MenuBook,
                                                contentDescription = null,
                                                modifier = Modifier.size(48.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(dept)
                                        }
                                    }
                                }
                            }
                        }

                        // STUDENT SECTION
                        "Student Section" -> {
                            val students = listOf(
                                "Siddhesh Sonar",
                                "Aryan Ganji",
                                "Yeash Dubey",
                                "Shubham Lambhor",
                                "Rudra Mali",
                                "Aryan Hulawle"
                            )

                            Column(modifier = Modifier.padding(16.dp)) {
                                students.forEach { student ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.TwoTone.AccountCircle,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(student, fontSize = 16.sp)
                                    }
                                }
                            }
                        }

                        // CONTACT US
                        "Contact Us" -> {
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize(),
                            ) {
                                Column(
                                    Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Contact",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clickable {
                                                val url =
                                                    "https://gpmumbai.ac.in/gpmweb/departments/computer-engineering/"
                                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                                context.startActivity(intent)
                                            }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Click here to view faculty page",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}
