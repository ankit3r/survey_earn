package com.gyanhub.surveyearnadmin.fragments.main

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.gyanhub.surveyearnadmin.adapter.SendBtn
import com.gyanhub.surveyearnadmin.adapter.UserAdapter
import com.gyanhub.surveyearnadmin.databinding.FragmentUsersBinding
import com.gyanhub.surveyearnadmin.factory.Factory
import com.gyanhub.surveyearnadmin.model.ProgrammesModel
import com.gyanhub.surveyearnadmin.model.UserModelModel
import com.gyanhub.surveyearnadmin.viewModel.GetUserData
import com.gyanhub.surveyearnadmin.viewModel.SetProgrammesData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersFragment(private var uid: String = "", private var num: Int) : Fragment(), SendBtn {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var userData: GetUserData
    private lateinit var addProgrammes: SetProgrammesData
    private val programmes = MutableLiveData<ProgrammesModel>()
    val getBalance: LiveData<ProgrammesModel>
        get() = programmes


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(layoutInflater, container, false)
        addProgrammes = ViewModelProvider(
            this,
            Factory(context as Activity, binding.layoutLoading, "addProgram")
        )[SetProgrammesData::class.java]
        mainFun()
        return binding.root
    }

    private fun mainFun() {
        rcViewSet()
    }

    private fun rcViewSet() {
        userData = ViewModelProvider(
            this,
            Factory(context as Activity, binding.layoutLoading, "user")
        )[GetUserData::class.java]
        binding.rcUser.layoutManager = LinearLayoutManager(activity)
        userData.getUser.observe(viewLifecycleOwner) {
            it?.let {
                val adapter =
                    UserAdapter(
                        it,
                        context as Activity,
                        binding.floatingActionButtonSend,
                        this,
                        num
                    )
                binding.rcUser.adapter = adapter
            }
        }
    }

    override fun UploadProgrammes(userData: ArrayList<UserModelModel>) {
        getProgrammesByUid(uid, binding.layoutLoading)
        getBalance.observe(this) {
            for (data in userData) {
                CoroutineScope(Dispatchers.IO).launch {
                    addProgrammes.addProgrammesInUserDB(it, uid, data.userID)
                }
            }
        }
    }

    private fun getProgrammesByUid(uid: String, view: LinearLayout) {
        view.visibility = View.VISIBLE
        FirebaseFirestore.getInstance().collection("Programmes").document(uid).get()
            .addOnSuccessListener {
                programmes.value = it.toObject(ProgrammesModel::class.java)!!
                view.visibility = View.GONE
            }.addOnFailureListener {
                view.visibility = View.GONE
            }
    }
}