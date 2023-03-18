@file:Suppress("UNCHECKED_CAST")

package com.gyanhub.surveyearnadmin.factory

import android.content.Context
import android.widget.LinearLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gyanhub.surveyearnadmin.viewModel.GetProgrammesData
import com.gyanhub.surveyearnadmin.viewModel.GetUserData
import com.gyanhub.surveyearnadmin.viewModel.SetProgrammesData

class Factory(
    private val context: Context,
    private val view: LinearLayout,
    private val model: String
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (model) {
            "user" -> GetUserData(context,view) as T
            "programmes" -> GetProgrammesData(context,view) as T
            "addProgram" -> SetProgrammesData(context,view) as T
//            "win" -> WinningViewModel(context) as T
//            "tick" -> TicketViewModel(context) as T
            else -> GetUserData(context,view) as T
        }
    }
}