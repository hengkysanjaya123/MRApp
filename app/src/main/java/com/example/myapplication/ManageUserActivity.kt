package com.example.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.MenuItem
import android.widget.Toast
import com.example.myapplication.Helper.DBOpenHelper
import com.example.myapplication.Model.User
import kotlinx.android.synthetic.main.activity_manage_user.*

class ManageUserActivity : AppCompatActivity() {

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_user)

        val action = intent.getStringExtra("action")
        if (action == "ADD") {
            this.title = "Add New User"
            btnAddUser.text = "ADD"
        } else if (action == "EDIT") {
            this.title = "Edit User"
            user = intent.getSerializableExtra("user") as User
            btnAddUser.text = "SAVE"

            etFullname.setText(user?.name)
            etEmail.setText(user?.email)
            etHandphone.setText(user?.phoneNumber)
            etPassword.setText(user?.password)
            etConfirmPassword.setText(user?.password)
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        btnAddUser.setOnClickListener {
            var fullname = etFullname.text.toString()
            var email = etEmail.text.toString()
            var phone = etHandphone.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (fullname.isEmpty()) {
                etFullname.error = "Fullname must be filled"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = "Email must be filled"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Email must be in correct format"
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                etHandphone.error = "Handphone must be filled"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Password must be filled"
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                etConfirmPassword.error = "Confirm Password must be filled"
                return@setOnClickListener
            }

            if (!password.equals(confirmPassword)) {
                etConfirmPassword.error = "Confirm password must be the same with password"
                return@setOnClickListener
            }

            val db = DBOpenHelper(this, null)
            if (btnAddUser.text == "ADD") {

                if (db.IsUserExist(email)) {
                    etEmail.error = "Email already exist"
                    return@setOnClickListener
                }

                var res = db.addUser(
                    User(
                        name = fullname,
                        email = email,
                        phoneNumber = phone,
                        role = "user",
                        password = password
                    )
                )
                if (res) {
                    Toast.makeText(this, "Data successfully added", Toast.LENGTH_SHORT).show()
                    setResult(1)
                    this.finish()
                }
            } else if (btnAddUser.text == "SAVE") {
                var res = db.editUser(
                    user!!,
                    User(name = fullname, email = email, phoneNumber = phone, role = "user", password = password)
                )
                if (res) {
                    Toast.makeText(this, "Data successfully updated", Toast.LENGTH_SHORT).show()
                    setResult(2)
                    this.finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
