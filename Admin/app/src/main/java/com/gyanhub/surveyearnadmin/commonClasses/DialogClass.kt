package com.gyanhub.surveyearnadmin.commonClasses

import android.app.AlertDialog
import android.content.Context

class DialogClass(private val context: Context) {
     fun dialog(title: String, massage: String) {

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(massage)
            .setCancelable(true)
            .setPositiveButton("Proceed") { _, _ ->

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }
}