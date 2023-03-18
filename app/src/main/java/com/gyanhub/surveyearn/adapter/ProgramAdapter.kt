package com.gyanhub.surveyearn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.model.UserProgrammes

class ProgramAdapter(
    private val programModel: ArrayList<UserProgrammes>, val context: Context,
    private val click: OnClick
) : RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder>() {
    class ProgramViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titles: TextView = view.findViewById(R.id.txtTitle)
        val earnPoint: TextView = view.findViewById(R.id.txtEarnPoint)
        val expiryTime: TextView = view.findViewById(R.id.txtExpTime)
        val totalTask: TextView = view.findViewById(R.id.txtTotalTask)
        val takeTime: TextView = view.findViewById(R.id.txtTakenTime)
        val status: TextView = view.findViewById(R.id.txtStatus)
        val startBtn: Button = view.findViewById(R.id.btnStart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.program_item, parent, false)
        return ProgramViewHolder(view)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
        val item = programModel[position]
        holder.titles.text = item.displayTitle
        holder.expiryTime.text = item.expiryTime
        holder.earnPoint.text = "earn up to  ${item.earnP} points"
        holder.totalTask.text = item.totalTask
        holder.takeTime.text = item.takeTime
        holder.startBtn.setOnClickListener {
            click.click(position, holder.status)
        }
    }

    override fun getItemCount(): Int {
        return programModel.size
    }
}

@Keep interface OnClick {
    fun click(position: Int, textView: TextView)
}