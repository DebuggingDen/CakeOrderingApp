package com.dden.cakeorderingapp.data

data class OrderUiState(
    val quantity: Int = 0,
    val flavour: String = "",
    val date: String = "",
    val price: String = "",
    val pickupOptions: List<String> = listOf()
)
