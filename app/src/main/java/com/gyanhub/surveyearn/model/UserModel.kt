package com.gyanhub.surveyearn.model

data class UserModel(
    val userName:String,
    val userGender:String,
    val userAge:String,
    val userCountry:String,
    val userEmail:String,
    val userID:String,
){constructor():this("","","","","","")}
