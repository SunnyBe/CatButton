package com.buchi.buttoner.resources

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.RestrictTo
import androidx.annotation.StyleableRes
import androidx.appcompat.content.res.AppCompatResources


/**
 * Utility methods to resolve resources for components.
 *
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
object ViewResources {

    /**
     * Returns the drawable object from the given attributes.
     *
     * <p>This method supports inflation of {@code <vector>} and {@code <animated-vector>} resources
     * on devices where platform support is not available.
     */
    fun getDrawable(
        context: Context, attributes: TypedArray, @StyleableRes index: Int
    ): Drawable? {
        if (attributes.hasValue(index)) {
            val resourceId = attributes.getResourceId(index, 0)
            if (resourceId != 0) {
                val value = AppCompatResources.getDrawable(context, resourceId)
                if (value != null) {
                    return value
                }
            }
        }
        return attributes.getDrawable(index)
    }

    /**
     * Returns the {@link ColorStateList} from the given {@link TypedArray} attributes. The resource
     * can include themeable attributes, regardless of API level.
     */
    fun getColorStateList(
        context: Context, attributes: TypedArray, @StyleableRes index: Int
    ): ColorStateList? {
        if (attributes.hasValue(index)) {
            val resourceId = attributes.getResourceId(index, 0)
            if (resourceId != 0) {
                val value = AppCompatResources.getColorStateList(context, resourceId)
                if (value != null) {
                    return value
                }
            }
        }

        // Reading a single color with getColorStateList() on API 15 and below doesn't always correctly
        // read the value. Instead we'll first try to read the color directly here.
        return attributes.getColorStateList(index)
    }

    /**
     * Draw the specified text to the center of the parent space available
     * @param canvas instance to draw the text. An instance of Canvas
     * @param paint to specify text position and color properties. An instance of Paint
     * @param textBound of the text being drawn. Value is an instance of Rect
     * @param text to be drawn with canvas.
     * @param extendX value of extension to text position on X-axis
     * @param extendY value of extension to text position on Y-axis
     */
    fun drawTextCenter(
        canvas: Canvas,
        paint: Paint,
        textBound: Rect,
        text: String,
        extendX: Float = 0F,
        extendY: Float = 0F
    ){
        canvas.getClipBounds(textBound)
        val cHeight: Int = textBound.height()
        val cWidth: Int = textBound.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(text, 0, text.length, textBound)
        val x: Float = cWidth / 2f - textBound.width() / 2f - textBound.left + extendX
        val y: Float = (cHeight / 2f + textBound.height() / 2f - textBound.bottom) + extendY
        canvas.drawText(text, x, y, paint)
    }

    /**
     * Calculates the height of the text that uses the specified paint instance
     * @param paint used by the text to get height for
     */
    fun textHeight(paint: Paint): Int{
        return (paint.fontMetricsInt.bottom - paint.fontMetricsInt.top)
    }
}