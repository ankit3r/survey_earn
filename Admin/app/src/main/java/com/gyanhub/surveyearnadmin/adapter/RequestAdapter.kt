package com.gyanhub.surveyearnadmin.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gyanhub.surveyearnadmin.R
import com.gyanhub.surveyearnadmin.model.userWithdrawModel

class RequestAdapter(private val withdraw:ArrayList<userWithdrawModel>):
    RecyclerView.Adapter<RequestAdapter.EGiftViewHolder>() {
    class EGiftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eGiftPrice: TextView = view.findViewById(R.id.txtEGiftPrice)
        val eGiftValue: TextView = view.findViewById(R.id.txtEGiftValue)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EGiftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_request, parent, false)
        return EGiftViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EGiftViewHolder, position: Int) {
        val currentItem = withdraw[position]
        holder.eGiftPrice.text = "$${currentItem.giftPrice}"
        holder.eGiftValue.text = "Value : ${currentItem.giftValue}"
    }

    override fun getItemCount(): Int {
        return withdraw.size
    }
}
