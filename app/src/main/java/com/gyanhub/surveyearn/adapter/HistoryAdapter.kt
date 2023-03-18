package com.gyanhub.surveyearn.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.model.HistoryModel

class HistoryAdapter(private val historyData:ArrayList<HistoryModel>)
    :RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){
    class HistoryViewHolder(view: View):RecyclerView.ViewHolder(view){
        val titles:TextView = view.findViewById(R.id.txtTitles)
        val earn:TextView = view.findViewById(R.id.txtAmount)
        val date:TextView = view.findViewById(R.id.txtDate)
        val send:TextView = view.findViewById(R.id.txtSend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item_view,parent,false)
        return HistoryViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
      val currentItem = historyData[position]
        if (currentItem.earn=="$3"){
            holder.titles.text = currentItem.titles
            holder.earn.text = currentItem.earn
            holder.date.text = currentItem.date
            holder.send.visibility = View.VISIBLE
        }else{
            holder.titles.text = currentItem.titles
            holder.earn.text = currentItem.earn
            holder.earn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.black_trophy, 0, 0, 0)
            holder.date.text = currentItem.date
            holder.send.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
       return historyData.size
    }

}