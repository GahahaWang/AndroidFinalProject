package com.example.sharedclassapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class Login : ComponentActivity() {
    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginbutton)

        loginButton.setOnClickListener {
//            val user = username.text.toString()
//            val pass = password.text.toString()
//
//            if(user == "123" && pass == "123"){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
//            }
//            else{
//                Toast.makeText(this, "wrong username or password", Toast.LENGTH_SHORT).show()
//            }
        }
    }
}

