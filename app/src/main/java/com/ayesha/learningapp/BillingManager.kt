package com.ayesha.learningapp

import android.content.Context

class BillingManager(
    private val context: Context,
    private val onProductsLoaded: (monthlyPrice: String, yearlyPrice: String) -> Unit,
    private val onPurchaseCompleted: (Transaction) -> Unit,
    private val onSubscriptionStatusChanged: (Boolean) -> Unit
) {

    private var isSubscribed = false

    init {
        loadDemoProducts()
    }

    private fun loadDemoProducts() {

        val monthlyPrice = "Rs. 499 / month"
        val yearlyPrice = "Rs. 4,999 / year"

        onProductsLoaded(
            monthlyPrice,
            yearlyPrice
        )
    }


    fun launchMonthlyPurchase() {

        val transaction = createTransaction(
            plan = "Monthly",
            amount = "Rs. 499"
        )

        isSubscribed = true

        onPurchaseCompleted(transaction)

        onSubscriptionStatusChanged(true)
    }


    fun launchYearlyPurchase() {

        val transaction = createTransaction(
            plan = "Yearly",
            amount = "Rs. 4,999"
        )

        isSubscribed = true

        onPurchaseCompleted(transaction)

        onSubscriptionStatusChanged(true)
    }


    private fun createTransaction(
        plan: String,
        amount: String
    ): Transaction {

        val transactionId =
            "DEMO-" + System.currentTimeMillis()

        val currentDate =
            java.text.SimpleDateFormat(
                "dd MMMM yyyy, hh:mm a",
                java.util.Locale.getDefault()
            ).format(
                java.util.Date()
            )

        return Transaction(
            transactionId = transactionId,
            plan = plan,
            amount = amount,
            status = "Successful",
            date = currentDate
        )
    }


    fun isUserSubscribed(): Boolean {

        return isSubscribed
    }


    fun cancelSubscription() {

        isSubscribed = false

        onSubscriptionStatusChanged(false)
    }


    fun endConnection() {

    }
}