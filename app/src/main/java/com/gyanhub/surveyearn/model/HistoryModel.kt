package com.gyanhub.surveyearn.model

data class HistoryModel(
    val programmesId: String,
    val titles: String,
    val earn: String,
    val date: String,
){constructor():this("","","","")}
