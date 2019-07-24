package com.example.myapplication.Model

import java.io.Serializable

data class User(
    var id: Int = -1,
    var name: String,
    var email: String,
    var phoneNumber: String,
    var password: String = "",
    var role : String
) : Serializable