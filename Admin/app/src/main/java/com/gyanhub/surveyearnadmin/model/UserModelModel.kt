package com.gyanhub.surveyearnadmin.model

data class UserModelModel(
    val userName: String,
    val userGender: String,
    val userAge: String,
    val userCountry: String,
    val userEmail: String,
    val userID: String,
) {
    constructor() : this("", "", "", "", "", "")
}