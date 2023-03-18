package com.gyanhub.surveyearn.model

data class StartModel(
    val img : String,
    val header : String,
    val paragraph: String,
    val term: String = "",
){
    constructor () : this("","","","")
}
