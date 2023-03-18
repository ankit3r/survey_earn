package com.gyanhub.surveyearn.model

data class BalanceModel(
    val points:Int,
    val mainBalance:Int
){constructor():this(0,0)}
