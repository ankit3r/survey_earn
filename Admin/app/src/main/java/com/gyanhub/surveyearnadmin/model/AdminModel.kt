package com.gyanhub.surveyearnadmin.model

data class AdminModel(  val userName: String,
                        val userEmail: String,
                        val userID: String,
) {
    constructor() : this("", "", "")
}