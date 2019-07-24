package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.myapplication.Helper.DBOpenHelper
import com.example.myapplication.Helper.currentUser
import com.example.myapplication.Model.User
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        SetAnimation()

        val db = DBOpenHelper(this, null)

//        db.deleteAllUsers()

        if (!db.IsUserExist("admin@gmail.com")) {
            db.addUser(
                User(
                    name = "Admin",
                    email = "admin@gmail.com",
                    phoneNumber = "1234567890",
                    password = "admin",
                    role = "admin"
                )
            )
        }
//        db.addUser(
//            User(
//                name = "test",
//                email = "test@gmail.com",
//                phoneNumber = "1234567890",
//                password = "test",
//                role = "user"
//            )
//        )

        btnInfo.setOnClickListener {
            val intt = Intent(this, AboutActivity::class.java)
            startActivity(intt)
        }

        btnLogin.setOnClickListener {
            //            currentUser = User(name = "test", email = "test@gmail.com", phoneNumber = "1233", password = "test")
//
//
//            return@setOnClickListener

            if (etEmail.text.isEmpty()) {
                etEmail.error = "Email must be filled"
                return@setOnClickListener
            }

            if (etPassword.text.isEmpty()) {
                etPassword.error = "Password must be filled"
                return@setOnClickListener
            }

            val u = User(-1, "", "", "", "", "")
            val res = db.login(etEmail.text.toString(), etPassword.text.toString(), u)
            Log.d("test:LoginActivity", res.toString())
            if (res) {
                currentUser = u

                if (currentUser?.role == "admin") {
                    val intt = Intent(this, AdminActivity::class.java)
                    startActivity(intt)
                } else if (currentUser?.role == "user") {
                    val intt = Intent(this, UserActivity::class.java)
                    startActivity(intt)
                }

            } else {
                //Snackbar.make(layout_login, "Sorry, Email and password incorrect", Snackbar.LENGTH_SHORT).show()
                Toast.makeText(this, "Sorry, Email and Password incorrect", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        SetAnimation()
    }

    fun SetAnimation() {
        iv_logo.animation = AnimationUtils.loadAnimation(this, R.anim.downtoup)
        layout_content.animation = AnimationUtils.loadAnimation(this, R.anim.downtoup)
    }
}
