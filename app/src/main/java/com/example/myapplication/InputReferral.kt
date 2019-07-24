package com.example.myapplication


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.Helper.DBOpenHelper
import com.example.myapplication.Helper.currentUser
import kotlinx.android.synthetic.main.fragment_input_referral.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class InputReferral : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_referral, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddReferral.setOnClickListener {
            var fullname = etFullname.text.toString()
            var email = etEmail.text.toString()
            var phone = etHandphone.text.toString()

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

            try {
                val db = DBOpenHelper(this.context!!, null)
                val res = db.addUserReferral(currentUser!!, fullname, email, phone)
                if (res) {
                    Toast.makeText(this.context!!, "Data Added, Thanks For your Referral", Toast.LENGTH_SHORT).show()
                }

                etFullname.setText("")
                etEmail.setText("")
                etHandphone.setText("")
                db.close()
            } catch (ex: Exception) {
                Log.d("test:InputReferral", "addReferralButton" + ex.toString())
            }
        }
    }
}
