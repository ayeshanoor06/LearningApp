package com.ayesha.learningapp

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class TransactionManager(
    context: Context
) {

    private val preferences = context.getSharedPreferences(
        "transaction_preferences",
        Context.MODE_PRIVATE
    )

    private val transactionsKey = "transactions"


    fun saveTransaction(transaction: Transaction) {

        val transactions = getTransactions()

        transactions.add(transaction)

        val jsonArray = JSONArray()

        transactions.forEach { item ->

            val jsonObject = JSONObject()

            jsonObject.put(
                "transactionId",
                item.transactionId
            )

            jsonObject.put(
                "plan",
                item.plan
            )

            jsonObject.put(
                "amount",
                item.amount
            )

            jsonObject.put(
                "status",
                item.status
            )

            jsonObject.put(
                "date",
                item.date
            )

            jsonArray.put(jsonObject)
        }

        preferences.edit()
            .putString(
                transactionsKey,
                jsonArray.toString()
            )
            .apply()
    }


    fun getTransactions(): MutableList<Transaction> {

        val transactions = mutableListOf<Transaction>()

        val savedData = preferences.getString(
            transactionsKey,
            null
        )

        if (savedData.isNullOrEmpty()) {
            return transactions
        }

        try {

            val jsonArray = JSONArray(savedData)

            for (i in 0 until jsonArray.length()) {

                val jsonObject = jsonArray.getJSONObject(i)

                val transaction = Transaction(

                    transactionId = jsonObject.getString(
                        "transactionId"
                    ),

                    plan = jsonObject.getString(
                        "plan"
                    ),

                    amount = jsonObject.getString(
                        "amount"
                    ),

                    status = jsonObject.getString(
                        "status"
                    ),

                    date = jsonObject.getString(
                        "date"
                    )
                )

                transactions.add(transaction)
            }

        } catch (e: Exception) {

            e.printStackTrace()
        }

        return transactions
    }


    fun clearTransactions() {

        preferences.edit()
            .remove(transactionsKey)
            .apply()
    }
}