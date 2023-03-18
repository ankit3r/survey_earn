package com.gyanhub.surveyearnadmin.model

data class UserBalanceModel(
    val points: Int,
    val mainBalance: Int
) {
    constructor() : this(0, 0)
}