package com.gyanhub.surveyearnadmin.model

data class UserHistoryModel(
    val programmesId: String,
    val titles: String,
    val earn: String,
    val date: String,
) {
    constructor() : this("", "", "", "")
}