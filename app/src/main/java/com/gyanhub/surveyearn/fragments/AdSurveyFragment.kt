package com.gyanhub.surveyearn.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearn.activitys.PaymentActivity
import com.gyanhub.surveyearn.adapter.EGiftCardAdapter
import com.gyanhub.surveyearn.adapter.OnClick
import com.gyanhub.surveyearn.databinding.FragmentAdSurveyBinding
import com.gyanhub.surveyearn.model.EgiftModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AdSurveyFragment(private val balance:Int) : Fragment(),OnClick {
private lateinit var binding:FragmentAdSurveyBinding
    private var data = ArrayList<EgiftModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
       binding = FragmentAdSurveyBinding.inflate(layoutInflater,container,false)
        addData()
        setRcView()

        return binding.root
    }

    private fun addData() {
        data.add(EgiftModel(250, 4))
        data.add(EgiftModel(400, 6))
        data.add(EgiftModel(600, 9))
        data.add(EgiftModel(850, 12))
    }

    private fun setRcView(){
        binding.rcECard.layoutManager = LinearLayoutManager(context)
        val adapter = EGiftCardAdapter(data,this)
        binding.rcECard.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun click(position: Int, textView: TextView) {
        buyGift(position)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buyGift(position: Int){
        val buyingAmount = data[position].giftPrice
        if (balance >= buyingAmount){
          dialog(position,buyingAmount)
        }else{
            Toast.makeText(context, "You haven't balance", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendRequest(requestAmount:Int,requestValue:Int){
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy   hh:mm"))
        val request = hashMapOf(
            "request" to "Withdraw Balance",
            "requestDate" to time,
            "requestAmount" to requestAmount,
            "requestValue" to requestValue,
        )
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).
            collection("request").
            add(request).addOnSuccessListener {
                binding.prog.visibility = View.GONE
                (context as PaymentActivity).balanceManage()
                Toast.makeText(context, "We'll send your gift card in seven working days on email id.", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                (context as PaymentActivity).balanceManage()
                binding.prog.visibility = View.GONE
                Toast.makeText(context, "Request failed please try again later.", Toast.LENGTH_SHORT).show()
            }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateBalance(remandBalance:Int, requestAmount:Int, requestValue:Int){
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).
            collection("balance").document("userbalance")
            .update("mainBalance",remandBalance).addOnCompleteListener {
                if (it.isSuccessful){
                   sendRequest(requestAmount,requestValue)
                }else{
                    Toast.makeText(context, "Request failed please try again later.", Toast.LENGTH_SHORT).show()
                    binding.prog.visibility = View.GONE
                }
            }.addOnFailureListener{
                (context as PaymentActivity).balanceManage()
                Toast.makeText(context, "Request failed please try again later.", Toast.LENGTH_SHORT).show()
                binding.prog.visibility = View.GONE
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dialog(position: Int,buyingAmount:Int) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Do you want to Buy Gift card?")
            .setCancelable(true)
            .setPositiveButton("Proceed") { _, _ ->
                binding.prog.visibility = View.VISIBLE
                updateBalance(balance - buyingAmount,buyingAmount,data[position].giftValue)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Buy Gift Alert")
        alert.show()
    }

}