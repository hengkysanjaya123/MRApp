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
import com.example.myapplication.Helper.DBOpenHelper
import com.example.myapplication.Helper.currentUser
import com.example.myapplication.Model.UserReferral
import kotlinx.android.synthetic.main.custom_layout_referral.view.*
import kotlinx.android.synthetic.main.fragment_user_view_referral.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class UserViewReferral : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_view_referral, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoadData()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d("test:UserViewReferral", "Enter onAttach")
    }


    override fun onResume() {
        super.onResume()
        Log.d("test:UserViewReferral", "Enter Resume")
        LoadData()
    }


    fun LoadData() {
        Log.d("test:UserViewReferral", "(LoadData)")
        val db = DBOpenHelper(this.context!!, null)

        val listReferral = db.getReferralByUser(currentUser!!)

        recyclerview_userreferral.layoutManager = LinearLayoutManager(this.context!!)
        recyclerview_userreferral.adapter = UserReferralAdapter(this.context!!, listReferral)

        db.close()
    }

    class UserReferralAdapter(
        var mContext: Context,
        var listReferral: ArrayList<UserReferral>
    ) : RecyclerView.Adapter<UserReferralAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.custom_layout_referral, p0, false))
        }

        override fun getItemCount(): Int {
            return listReferral.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
            val current = listReferral[p1]

            holder.tvName.text = current.name
            holder.tvEmail.text = current.email
            holder.tvPhoneNumber.text = current.phoneNumber
        }


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName = itemView.tvReferralName
            val tvEmail = itemView.tvReferralEmail
            val tvPhoneNumber = itemView.tvReferralPhoneNumber
        }
    }

}
