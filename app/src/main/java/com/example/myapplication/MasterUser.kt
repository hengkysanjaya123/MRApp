package com.example.myapplication


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.myapplication.Helper.DBOpenHelper
import com.example.myapplication.Model.User
import kotlinx.android.synthetic.main.custom_layout_user.view.*
import kotlinx.android.synthetic.main.fragment_master_user.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MasterUser : Fragment() {

    var listUser: ArrayList<User> = arrayListOf(

    )

    var userAdapter: UserAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_master_user, container, false)
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAdapter = UserAdapter(this, this.context, listUser) {
            LoadData()
        }
        recyclerview_user.layoutManager = LinearLayoutManager(this.context)
        recyclerview_user.adapter = userAdapter


        val dbHandler = DBOpenHelper(this.context!!, null)
//        dbHandler.deleteAllUsers()
//        dbHandler.addUser(User(name = "coba", email = "coba@gmai.com", phoneNumber = "123456"))
//        dbHandler.addUser(User(2, "abc", "abc@gmai.com", "123456"))
        LoadData()

        recyclerview_user.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0 && fabAddUser.visibility === View.VISIBLE) {
                    fabAddUser.hide()
                } else if (dy < 0 && fabAddUser.visibility !== View.VISIBLE) {
                    fabAddUser.show()
                }
            }
        })

        fabAddUser.setOnClickListener {
            val intt = Intent(this.context!!, ManageUserActivity::class.java)
            intt.putExtra("action", "ADD")
            startActivityForResult(intt, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // add User
        if (resultCode == 1) {
            LoadData();
        }
        // edit user
        else if (resultCode == 2) {
            LoadData()
        }
    }

    fun LoadData() {
        val dbHandler = DBOpenHelper(this.context!!, null)

        listUser.clear()
        listUser.addAll(dbHandler.getUsers())

        if (listUser.size == 0) {
            tvEmptyStatus.visibility = View.VISIBLE
            recyclerview_user.visibility = View.INVISIBLE
        } else {
            tvEmptyStatus.visibility = View.INVISIBLE
            recyclerview_user.visibility = View.VISIBLE
        }

        userAdapter?.notifyDataSetChanged()
    }

    class UserAdapter(
        var masterUser: MasterUser,
        var mContext: Context?,
        var listUser: List<User>,
        var callBack: () -> Unit
    ) :
        RecyclerView.Adapter<UserAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.custom_layout_user, p0, false))
        }

        override fun getItemCount(): Int {
            return listUser.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
            var user = listUser[p1]

            holder.tvName.text = user.name
            holder.tvEmail.text = user.email
            holder.tvPhoneNumber.text = user.phoneNumber
            holder.userCardView.tag = user

            holder.btnDelete.setOnClickListener {
                val msg = AlertDialog.Builder(this.mContext).create()
                msg.setTitle("Confirmation")
                msg.setMessage("Are you sure to delete this data ?")
                msg.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { dialog, which ->
                    val db = DBOpenHelper(this.mContext!!, null)

                    db.deleteUser(user)
                    Toast.makeText(this.mContext, "Data successfully deleted", Toast.LENGTH_SHORT).show()
                    callBack()
                }

                msg.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, which ->

                }
                msg.show()
            }

            holder.btnEdit.setOnClickListener {
                val intt = Intent(this.mContext, ManageUserActivity::class.java)
                intt.putExtra("action", "EDIT")
                intt.putExtra("user", user)
                this.masterUser.startActivityForResult(intt, 2)
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val userCardView = itemView.userCardView
            val ivLogo = itemView.iv_user_logo
            val tvName = itemView.tv_user_name
            val tvEmail = itemView.tv_user_email
            val tvPhoneNumber = itemView.tv_user_phonenumber
            val btnDelete = itemView.iv_user_delete_action
            val btnEdit = itemView.iv_user_edit_action
        }
    }

}
