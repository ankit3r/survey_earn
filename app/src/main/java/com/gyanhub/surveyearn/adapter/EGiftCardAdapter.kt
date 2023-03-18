package com.gyanhub.surveyearn.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gyanhub.surveyearn.R
import com.gyanhub.surveyearn.model.EgiftModel

class EGiftCardAdapter(private val data: ArrayList<EgiftModel>,private val onClick: OnClick) :
    RecyclerView.Adapter<EGiftCardAdapter.EGiftViewHolder>() {
    class EGiftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eGiftPrice: TextView = view.findViewById(R.id.txtEGiftPrice)
        val eGiftValue: TextView = view.findViewById(R.id.txtEGiftValue)
        val btnBuy: TextView = view.findViewById(R.id.txtBuy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EGiftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.e_gift_item, parent, false)
        return EGiftViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EGiftViewHolder, position: Int) {
        val currentItem = data[position]
        holder.eGiftPrice.text = "$${currentItem.giftPrice}"
        holder.eGiftValue.text = "Value : ${currentItem.giftValue}"
        holder.btnBuy.setOnClickListener {
            onClick.click(position,holder.btnBuy)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
