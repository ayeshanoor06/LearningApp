package com.ayesha.learningapp

data class Transaction(
    val transactionId: String,
    val plan: String,
    val amount: String,
    val status: String,
    val date: String
)