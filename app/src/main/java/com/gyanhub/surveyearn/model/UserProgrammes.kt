package com.gyanhub.surveyearn.model

data class UserProgrammes(
    val programID: String,
    val title: String,
    val displayTitle: String,
    val earnP: Int,
    val expiryTime: String,
    val totalTask: String,
    val takeTime: String,
    val url: String,
    val docID: String,
) {
    constructor() : this("", "", "", 0, "", "","", "", "")
}
