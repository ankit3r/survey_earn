package com.gyanhub.surveyearnadmin.model

data class userWithdrawModel(
    val giftValue: Int,
    val giftPrice: Int,
    val request : String,
    val requestDate : String,
) {
    constructor() : this(0, 0,"","")
}