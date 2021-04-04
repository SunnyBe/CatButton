package com.buchi.buttoner.buttons

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.Px
import androidx.core.content.res.ResourcesCompat
import com.buchi.buttoner.R
import com.buchi.buttoner.resources.ViewResources
import com.google.android.material.button.MaterialButton


class CatButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr) {
    private val textBound = Rect()

    // Description text
    private var descText: String? = null
    private var descTextSize: Float = 0F
    private var descTextColor: Int = Color.BLACK
    private var descTextRec = Rect()
    private var descTextPaint = Paint().apply {
        isAntiAlias = true
        gravity = Gravity.CENTER_HORIZONTAL
    }

    // Icon desc
    @MaterialButton.IconGravity
    private val iconGravity = 0
    private var iconTintMode: PorterDuff.Mode? = null
    private var iconTint: ColorStateList? = null
    private var icon: Drawable?

    @Px
    private var iconSize: Int? = null
    @Px
    private val iconPadding: Int = 0
    private var iconBitmap: Bitmap? = null
    private var iconStartMargin = 50f
    private val iconPaint = Paint().apply {
        isAntiAlias = true
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DynamicButton,
            0, 0
        )

        descText = typedArray.getString(R.styleable.DynamicButton_desc_text)
        descTextSize =
            typedArray.getDimension(R.styleable.DynamicButton_desc_text_size, textSize - 2F)

        icon = ViewResources.getDrawable(context, typedArray, R.styleable.DynamicButton_icon)
        iconSize = typedArray.getDimensionPixelSize(R.styleable.DynamicButton_iconSize, 16)

        // Todo for test purpose
        text = "Button"
        descText = "Desc For long"
//        icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_account, null)
        gravity = Gravity.CENTER
        setIcon(icon)
        setDescription(descText)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val iconIntrWidth = if (icon != null) icon!!.intrinsicWidth else 0
        val iconIntrHeight = if (icon != null) icon!!.intrinsicHeight else 0

        val longestText = longestTextWidth()
        val minWidth = measuredWidth.coerceAtLeast(longestText + paddingRight + paddingStart + paddingTop + paddingLeft + iconIntrWidth)
        val minHeight = measuredHeight.coerceAtLeast(textHeight() + if (descText != null) textHeight() else 0 + paddingTop + paddingBottom + iconIntrHeight)
        setMeasuredDimension(minWidth, minHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.getTextBounds(text.toString(), 0, text.length, textBound)
        val textY = baseline + textBound.top + paddingTop
        val textX = (icon?.intrinsicWidth?:0).toFloat() + paddingStart

        descText?.run {
            canvas?.let {
                if (descText !=null) {
                    drawTextCenter(canvas, descTextPaint, this, 0F, textY.toFloat())
                }
            }
        }
    }

    private fun drawTextCenter(
        canvas: Canvas,
        paint: Paint,
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

    private fun textHeight(): Int{
        return paint.fontMetricsInt.bottom - paint.fontMetricsInt.top
    }

    private fun longestTextWidth(): Int {
        val buttonTextSize = paint.measureText(text.toString()).toInt()
        val descTextSize = descText?.let { paint.measureText(descText).toInt() } ?: 0
        return if (buttonTextSize > descTextSize) buttonTextSize else descTextSize
    }

    private fun setDescription(text: String?) {
        text?.let { txt ->
            descText = txt
        }
        postInvalidate()
    }

    private fun setIcon(icon: Drawable?) {
        icon?.apply {
            setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
            setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
        }
        postInvalidate()
    }
}