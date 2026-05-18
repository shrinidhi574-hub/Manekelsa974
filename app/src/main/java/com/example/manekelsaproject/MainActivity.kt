package com.example.manekelsaproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

data class Worker(
    val id: String = "",
    val name: String = "",
    val job: String = "",
    val phone: String = "",
    val location: String = "",
    val rating: String = "",
    val imageUrl: String = ""
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ManeKelsaApp()
        }
    }
}

@Composable
fun ManeKelsaApp() {

    val navController = rememberNavController()

    val auth = FirebaseAuth.getInstance()

    val startDestination =
        if (auth.currentUser != null) {
            "home"
        } else {
            "login"
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("workers") {
            WorkersScreen()
        }

        composable("admin") {
            AdminScreen()
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = rememberAsyncImagePainter(
                "https://images.unsplash.com/photo-1522202176988-66273c2fd55f"
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            verticalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = "👷 Mane Kelsa Login",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = {
                            Text("Email")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = {
                            Text("Password")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {

                            auth.signInWithEmailAndPassword(
                                email,
                                password
                            )
                                .addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Login Success",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate("home") {
                                        popUpTo("login") {
                                            inclusive = true
                                        }
                                    }
                                }
                                .addOnFailureListener {

                                    Toast.makeText(
                                        context,
                                        it.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Login")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            navController.navigate("register")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Go To Register")
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(navController: NavHostController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = rememberAsyncImagePainter(
                "https://images.unsplash.com/photo-1504384308090-c894fdcc538d"
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            verticalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = "👷 Mane Kelsa Register",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = {
                            Text("Email")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = {
                            Text("Password")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {

                            auth.createUserWithEmailAndPassword(
                                email,
                                password
                            )
                                .addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Register Success",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate("login")
                                }
                                .addOnFailureListener {

                                    Toast.makeText(
                                        context,
                                        it.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Create Account")
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "👷 Mane Kelsa",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                navController.navigate("workers")
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("👷 Available Workers")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                navController.navigate("admin")
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("👑 Admin Panel")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {

                auth.signOut()

                navController.navigate("login") {
                    popUpTo("home") {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("🚪 Logout")
        }
    }
}

@Composable
fun AdminScreen() {

    val context = LocalContext.current

    val database =
        FirebaseDatabase.getInstance()
            .reference
            .child("workers")

    val storage =
        FirebaseStorage.getInstance()
            .reference
            .child("worker_images")

    var name by remember { mutableStateOf("") }
    var job by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) {
            imageUri = it
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {

            Text(
                text = "👑 Admin Panel",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Worker Name")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = job,
                onValueChange = {
                    job = it
                },
                label = {
                    Text("Job Type")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                },
                label = {
                    Text("Phone Number")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                },
                label = {
                    Text("Location")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = rating,
                onValueChange = {
                    rating = it
                },
                label = {
                    Text("Rating")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    launcher.launch("image/*")
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("📷 Select Worker Photo")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {

                    val key =
                        database.push().key ?: return@Button

                    fun saveData(url: String) {

                        val worker = Worker(
                            id = key,
                            name = name,
                            job = job,
                            phone = phone,
                            location = location,
                            rating = rating,
                            imageUrl = url
                        )

                        database.child(key)
                            .setValue(worker)
                            .addOnSuccessListener {

                                Toast.makeText(
                                    context,
                                    "Worker Registered",
                                    Toast.LENGTH_SHORT
                                ).show()

                                name = ""
                                job = ""
                                phone = ""
                                location = ""
                                rating = ""
                            }
                    }

                    if (imageUri != null) {

                        val ref =
                            storage.child(
                                UUID.randomUUID().toString()
                            )

                        ref.putFile(imageUri!!)
                            .addOnSuccessListener {

                                ref.downloadUrl
                                    .addOnSuccessListener {

                                        saveData(it.toString())
                                    }
                            }

                    } else {

                        saveData("")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("✅ Register Worker")
            }
        }
    }
}

@Composable
fun WorkersScreen() {

    val context = LocalContext.current

    val database =
        FirebaseDatabase.getInstance()
            .reference
            .child("workers")

    val workersList =
        remember { mutableStateListOf<Worker>() }

    LaunchedEffect(Unit) {

        database.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    workersList.clear()

                    for (child in snapshot.children) {

                        val worker = Worker(
                            id = child.key ?: "",
                            name = child.child("name").value?.toString() ?: "",
                            job = child.child("job").value?.toString() ?: "",
                            phone = child.child("phone").value?.toString() ?: "",
                            location = child.child("location").value?.toString() ?: "",
                            rating = child.child("rating").value?.toString() ?: "",
                            imageUrl = child.child("imageUrl").value?.toString() ?: ""
                        )

                        workersList.add(worker)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {

            Text(
                text = "👷 Available Workers",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        items(workersList) { worker ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),

                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    if (worker.imageUrl.isNotEmpty()) {

                        Image(
                            painter =
                                rememberAsyncImagePainter(
                                    worker.imageUrl
                                ),

                            contentDescription = null,

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(
                                    RoundedCornerShape(12.dp)
                                ),

                            contentScale =
                                ContentScale.Crop
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )
                    }

                    Text("👤 ${worker.name}")
                    Text("💼 ${worker.job}")
                    Text("📞 ${worker.phone}")
                    Text("📍 ${worker.location}")
                    Text("⭐ ${worker.rating}")

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Button(
                        onClick = {

                            val cleanNumber =
                                worker.phone
                                    .replace("+", "")
                                    .replace(" ", "")
                                    .replace("-", "")

                            val intent =
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(
                                        "https://api.whatsapp.com/send?phone=$cleanNumber"
                                    )
                                )

                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("💬 WhatsApp")
                    }
                }
            }
        }
    }
}