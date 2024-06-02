package com.example.pelis.ui.theme

fun authenticate(username: String, password: String): Boolean {
    return username == "Admin" && password == "Password*123"
}