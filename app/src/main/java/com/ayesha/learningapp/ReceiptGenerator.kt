package com.ayesha.learningapp

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

object ReceiptGenerator {

    fun generateReceipt(
        context: Context,
        transaction: Transaction
    ): File {

        val pdfDocument = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(
            595,
            842,
            1
        ).create()

        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint().apply {
            isAntiAlias = true
        }

        var y = 80f

        // App title
        paint.textSize = 26f
        paint.isFakeBoldText = true

        canvas.drawText(
            "Learning App",
            50f,
            y,
            paint
        )

        y += 35f

        paint.textSize = 18f
        paint.isFakeBoldText = false

        canvas.drawText(
            "Payment Receipt",
            50f,
            y,
            paint
        )

        y += 50f

        // Receipt details
        paint.textSize = 16f

        canvas.drawText(
            "Transaction Details",
            50f,
            y,
            paint
        )

        y += 35f

        canvas.drawText(
            "Plan: ${transaction.plan}",
            50f,
            y,
            paint
        )

        y += 30f

        canvas.drawText(
            "Amount: ${transaction.amount}",
            50f,
            y,
            paint
        )

        y += 30f

        canvas.drawText(
            "Transaction ID:",
            50f,
            y,
            paint
        )

        y += 25f

        canvas.drawText(
            transaction.transactionId,
            50f,
            y,
            paint
        )

        y += 30f

        canvas.drawText(
            "Date: ${transaction.date}",
            50f,
            y,
            paint
        )

        y += 30f

        canvas.drawText(
            "Status: ${transaction.status}",
            50f,
            y,
            paint
        )

        y += 60f

        paint.textSize = 14f

        canvas.drawText(
            "Thank you for using Learning App.",
            50f,
            y,
            paint
        )

        y += 25f

        canvas.drawText(
            "This is an app-generated receipt.",
            50f,
            y,
            paint
        )

        pdfDocument.finishPage(page)

        val receiptsDirectory = File(
            context.filesDir,
            "receipts"
        )

        if (!receiptsDirectory.exists()) {
            receiptsDirectory.mkdirs()
        }

        val fileName =
            "receipt_${transaction.transactionId}.pdf"

        val receiptFile = File(
            receiptsDirectory,
            fileName
        )

        FileOutputStream(receiptFile).use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }

        pdfDocument.close()

        return receiptFile
    }
}