package com.example.myapplication.Model

import java.io.Serializable

data class UserReferral(val id: Int = -1, val name: String, val email: String, val phoneNumber: String, val userId : Int, val user_referrer_name : String) : Serializable