package com.ayesha.learningapp

import android.content.Context

class BillingManager(
    private val context: Context,
    private val onProductsLoaded: (monthlyPrice: String, yearlyPrice: String) -> Unit,
    private val onPurchaseCompleted: () -> Unit,
    private val onSubscriptionStatusChanged: (Boolean) -> Unit
) {

    private var isSubscribed = false

    init {
        loadDemoProducts()
    }

    /**
     * Loads demo subscription prices.
     *
     * This version does not connect to Google Play Billing.
     * It is completely free and works locally for development,
     * demonstrations and internship project testing.
     */
    private fun loadDemoProducts() {

        val monthlyPrice = "Rs. 499 / month"
        val yearlyPrice = "Rs. 4,999 / year"

        onProductsLoaded(
            monthlyPrice,
            yearlyPrice
        )
    }

    /**
     * Simulates a monthly subscription purchase.
     */
    fun launchMonthlyPurchase() {

        isSubscribed = true

        onPurchaseCompleted()
        onSubscriptionStatusChanged(true)
    }

    /**
     * Simulates a yearly subscription purchase.
     */
    fun launchYearlyPurchase() {

        isSubscribed = true

        onPurchaseCompleted()
        onSubscriptionStatusChanged(true)
    }

    /**
     * Returns the current demo subscription status.
     */
    fun isUserSubscribed(): Boolean {
        return isSubscribed
    }

    /**
     * Cancels the demo subscription.
     */
    fun cancelSubscription() {

        isSubscribed = false

        onSubscriptionStatusChanged(false)
    }

    /**
     * Closes the billing manager.
     *
     * There is no real billing connection in this free demo version.
     */
    fun endConnection() {
        // Nothing to close in demo mode.
    }
}