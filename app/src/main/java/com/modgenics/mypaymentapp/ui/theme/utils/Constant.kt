package com.modgenics.mypaymentapp.ui.theme.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.PopupWindow
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


object Constant {

    const val API_KEY = "rzp_live_FJpeuNPo9t3LI4"


    fun captureComposableAsBitmap(context: Context, content: @Composable () -> Unit): Bitmap {
        val composeView = ComposeView(context).apply {
            setContent { content() }
        }

        // Attach the ComposeView to a PopupWindow
        val popupWindow = PopupWindow(context)
        popupWindow.contentView = composeView
        popupWindow.width = 1080 // Set your desired width
        popupWindow.height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        popupWindow.showAtLocation(View(context), 0, 0, 0)

        // Measure and layout the ComposeView
        composeView.measure(
            View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        composeView.layout(0, 0, composeView.measuredWidth, composeView.measuredHeight)

        // Create the Bitmap
        val bitmap = Bitmap.createBitmap(
            composeView.width,
            composeView.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(bitmap)
        composeView.draw(canvas)

        // Dismiss the PopupWindow to clean up
        popupWindow.dismiss()

        return bitmap
    }

    fun shareBitmap(context: Context, bitmap: Bitmap) {
        val file = File(context.cacheDir, "shared_card.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            setPackage("com.whatsapp") // For WhatsApp specifically
        }

        context.startActivity(Intent.createChooser(intent, "Share Visiting Card"))
    }


}