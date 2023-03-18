package com.gyanhub.surveyearnadmin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gyanhub.surveyearnadmin.R
import com.gyanhub.surveyearnadmin.model.UserHistoryModel

class HistoryAdapter(private val history: ArrayList<UserHistoryModel>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titles: TextView = view.findViewById(R.id.txtTitles)
        val earn: TextView = view.findViewById(R.id.txtAmount)
        val date: TextView = view.findViewById(R.id.txtDate)
        val send: TextView = view.findViewById(R.id.txtSend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_history, parent, false)
        return HistoryViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = history[position]
        if (currentItem.earn == "$3") {
            holder.titles.text = currentItem.titles
            holder.earn.text = currentItem.earn
            holder.date.text = currentItem.date
            holder.send.visibility = View.VISIBLE
        } else {
            holder.titles.text = currentItem.titles
            holder.earn.text = currentItem.earn
            holder.earn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.black_trophy, 0, 0, 0)
            holder.date.text = currentItem.date
            holder.send.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return history.size
    }

}