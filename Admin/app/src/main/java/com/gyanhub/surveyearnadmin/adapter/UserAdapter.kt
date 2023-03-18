package com.gyanhub.surveyearnadmin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gyanhub.surveyearnadmin.R
import com.gyanhub.surveyearnadmin.activity.FragmentHolderActivity
import com.gyanhub.surveyearnadmin.model.UserModelModel

class UserAdapter(
    private val userData: ArrayList<UserModelModel>,
    private val context: Context,
    private val btnSend: FloatingActionButton,
    private val send: SendBtn,
    private val id: Int

) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    var isSelectMode: Boolean = false
    var selectData = ArrayList<UserModelModel>()

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName = view.findViewById<TextView>(R.id.txtUserName)
        val userEmail = view.findViewById<TextView>(R.id.txtUserEmail)
        val userCountry = view.findViewById<TextView>(R.id.txtUserCountry)
        val userGender = view.findViewById<TextView>(R.id.txtUserGender)
        val itemRoot = view.findViewById<RelativeLayout>(R.id.itemRoot)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userData[position]
        holder.userName.text = currentUser.userName
        holder.userEmail.text = currentUser.userEmail
        holder.userCountry.text = currentUser.userCountry
        holder.userGender.text = currentUser.userGender
        when(id){
            0->{
                holder.itemRoot.setOnClickListener {
                    val i = Intent(context,FragmentHolderActivity::class.java)
                    i.putExtra("id",2)
                    i.putExtra("userID",currentUser.userID)
                    context.startActivity(i)
                }
            }
            1->{
                holder.itemRoot.setOnLongClickListener { view ->
                    (selecr(view, position))
                }
                holder.itemRoot.setOnClickListener { click(it, position) }
                btnSend.setOnClickListener { send.UploadProgrammes(selectData) }
            }
        }
    }

    private fun click(view: View, position: Int) {
        if (isSelectMode) {
            if (selectData.contains(userData[position])) {
                view.isSelected = false
                selectData.remove(userData[position])
            } else {
                view.isSelected = true
                selectData.add(userData[position])
            }
            if (selectData.size == 0) {
                btnSend.visibility = View.GONE
                isSelectMode = false
            }
        } else {
            Toast.makeText(context, "Select one item to Long Click. ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selecr(view: View, position: Int): Boolean {
        isSelectMode = true
        btnSend.visibility = View.VISIBLE
        if (selectData.contains(userData[position])) {
            view.isSelected = false
            selectData.remove(userData[position])
        } else {
            view.isSelected = true
            selectData.add(userData[position])
        }
        if (selectData.size == 0) {
            btnSend.visibility = View.GONE
            isSelectMode = false
        }
        return true
    }


    override fun getItemCount(): Int {
        return userData.size
    }
}

interface SendBtn {
    fun UploadProgrammes(userData: ArrayList<UserModelModel>)
}