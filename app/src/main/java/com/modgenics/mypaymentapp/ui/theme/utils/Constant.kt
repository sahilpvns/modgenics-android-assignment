package com.modgenics.mypaymentapp.ui.theme.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object Constant {

    const val API_KEY = "rzp_live_FJpeuNPo9t3LI4"

    fun captureAndShareVisitingCard(
        context: Context,
        cardView: View,
        username: String?,
        designation: String?,
        mobile: String?,
        email: String?,
        company: String?
    ) {
        // Step 1: Create a bitmap of the cardView
        val bitmap = Bitmap.createBitmap(cardView.width, cardView.height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        cardView.draw(canvas)

        // Step 2: Save the bitmap to a file
        val file = File(context.cacheDir, "visiting_card.png")
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        // Step 3: Share the image via WhatsApp
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, "Check out this Visiting Card!")
            `package` = "com.whatsapp" // Limit sharing to WhatsApp
        }

        try {
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp not installed.", Toast.LENGTH_SHORT).show()
        }
    }


}