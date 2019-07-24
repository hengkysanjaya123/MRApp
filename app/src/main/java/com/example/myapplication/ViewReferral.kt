package com.example.myapplication


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.myapplication.Helper.DBOpenHelper
import com.example.myapplication.Model.User
import com.example.myapplication.Model.UserReferral
import kotlinx.android.synthetic.main.custom_layout_viewreferral.view.*
import kotlinx.android.synthetic.main.fragment_view_referral.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ViewReferral : Fragment() {

    lateinit var db: DBOpenHelper
    var formReady = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_referral, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            db = DBOpenHelper(this.context!!, null)
            val listUsers = db.getUsers()
            listUsers.add(0, User(-1, "", "All Users", "", "", ""))

            var spinnerAdapter = SpinnerAdapter(this.context!!, listUsers)
            spinner_referrer.adapter = spinnerAdapter
            spinner_referrer.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val user = listUsers[position]
                    LoadData(user)
                }
            }
        } catch (ex: Exception) {
            Log.d("test:ViewReferral", "(OnViewCreated)" + ex.toString())
        }

        formReady = true
        LoadData()
    }

    private fun LoadData(user: User? = null) {
        if (!formReady) return
        try {
            var listReferral = arrayListOf<UserReferral>()

            if (user == null || user?.email == "All Users") {
                listReferral = db.getAllReferrals()
            } else {
                listReferral = db.getReferralByUser(user)
            }

            if (listReferral.size == 0) {
                tvEmptyStatus.visibility = View.VISIBLE
                recyclerview_viewreferral.visibility = View.INVISIBLE
            } else {

                tvEmptyStatus.visibility = View.INVISIBLE
                recyclerview_viewreferral.visibility = View.VISIBLE

                Log.d("test:ViewReferral", "(onCreated) list size : " + listReferral.count())
                recyclerview_viewreferral.layoutManager = LinearLayoutManager(this.context!!)
                recyclerview_viewreferral.adapter = UserReferralAdminAdapter(this.context!!, listReferral)
            }

        } catch (ex: Exception) {
            Log.d("test:ViewReferral", "onCreated" + ex.toString())
        }
    }


    class SpinnerAdapter(var mContext: Context, var listUser: ArrayList<User>) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val current = listUser[position]
            val label = TextView(mContext)
            label.setPadding(25, 25, 25, 25)
            var text = current.email
            if (current.name != "") {
                text += " - " + current.name
            }
            label.setText(text)

            return label
        }

        override fun getItem(position: Int): Any {
            return listUser[position]
        }

        override fun getItemId(position: Int): Long {
            return -1
        }

        override fun getCount(): Int {
            return listUser.size
        }


    }

    class UserReferralAdminAdapter(
        var mContext: Context,
        var listReferral: ArrayList<UserReferral>
    ) : RecyclerView.Adapter<UserReferralAdminAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.custom_layout_viewreferral, p0, false))
        }

        override fun getItemCount(): Int {
            return listReferral.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
            val current = listReferral[p1]

            Log.d("test:ViewReferral", "Referral Data : " + current.toString())

            holder.tvName.text = current.name
            holder.tvEmail.text = current.email
            holder.tvPhoneNumber.text = current.phoneNumber
            holder.tvReferrer.text = current.user_referrer_name
        }


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName = itemView.tvReferralName
            val tvEmail = itemView.tvReferralEmail
            val tvPhoneNumber = itemView.tvReferralPhoneNumber
            val tvReferrer = itemView.tvReferrer
        }
    }
}

