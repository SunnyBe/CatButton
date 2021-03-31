package com.buchi.buttoner.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes

object Utils {

    /**
     * Convert int Resource to Bitmap using an instance of the context
     * @param context
     * @param drawable to be converted to Bitmap
     */
    fun convertIconToBitmap(context: Context, @DrawableRes drawable: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, drawable)
    }
}