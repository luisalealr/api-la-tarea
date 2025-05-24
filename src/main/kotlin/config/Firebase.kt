package com.example.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileInputStream

fun initializeFirebase() {
    val serviceAccount = {}::class.java.classLoader.getResourceAsStream("serviceAccountKey.json")
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    FirebaseApp.initializeApp(options)
}


suspend fun verifyFirebaseToken(token: String): FirebaseToken? = withContext(Dispatchers.IO) {
    try {
        FirebaseAuth.getInstance().verifyIdToken(token)
    } catch (e: Exception) {
        null
    }
}
