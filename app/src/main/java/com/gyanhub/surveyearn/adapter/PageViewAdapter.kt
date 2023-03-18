package com.gyanhub.surveyearn.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.model.StartModel

class PageViewAdapter(
    private val data: ArrayList<StartModel>,
    private val context: Context,
    private val proView: LinearLayout
) : RecyclerView.Adapter<PageViewAdapter.PageViewHolder>() {

    class PageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: LottieAnimationView = view.findViewById(R.id.img_av)
        val head: TextView = view.findViewById(R.id.txt_h)
        val para: TextView = view.findViewById(R.id.txt_para)
        val term: TextView = view.findViewById(R.id.txt_Term)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_img, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val item = data[position]
        holder.img.setAnimation(item.img)
        holder.head.text = item.header
        holder.para.text = item.paragraph

        if (item.term == "") {
            holder.term.visibility = View.GONE
        } else {
            holder.term.visibility = View.VISIBLE
            holder.term.text = item.term
            holder.term.setOnClickListener {
                proView.visibility = View.VISIBLE
                FirebaseFirestore.getInstance().collection("settings")
                    .document("settingsDOC").get().addOnSuccessListener {
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse(it.data?.get("termOfService").toString())
                        proView.visibility = View.GONE
                        context.startActivity(openURL)
                    }.addOnFailureListener{
                        proView.visibility = View.GONE
                        Toast.makeText(context, "Try again....", Toast.LENGTH_SHORT).show()
                    }

            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
