package com.buchi.buttoner.buttons

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.content.res.ResourcesCompat
import com.buchi.buttoner.R
import com.buchi.buttoner.resources.ViewResources


class CatButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr) {
    private var mDownTouch = false
    private val currentBg: Drawable = background
    private val textBound = Rect()

    // Description text
    private var descText: String? = null
    private var descTextSize: Float = 14F
    private var descTextColor: Int? = null
    private var descTextRec = Rect()
    private var descTextPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    // Icon desc
    @IconGravity
    private val iconGravity = 0
    private var iconTintMode: PorterDuff.Mode? = null
    private var iconTint: ColorStateList? = null
    private var icon: Drawable? = null

    @Px
    var iconSize: Int? = null

    @Px
    var iconPadding: Int = 0
    var iconBitmap: Bitmap? = null
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

        if (attrs != null) {
            descText = typedArray.getString(R.styleable.DynamicButton_desc_text)
            descTextSize =
                typedArray.getDimension(R.styleable.DynamicButton_desc_text_size, textSize - 2F)
            descTextColor = typedArray.getColor(R.styleable.DynamicButton_desc_textColor, Color.BLACK)

            icon = ViewResources.getDrawable(context, typedArray, R.styleable.DynamicButton_icon)
            iconSize = typedArray.getDimensionPixelSize(R.styleable.DynamicButton_iconSize, 16)
        }

        gravity = Gravity.CENTER
        descTextPaint.textAlign = Paint.Align.CENTER
        descTextPaint.color = descTextColor ?: Color.BLACK
        isFocusable = true
        isClickable = true
        elevation = 8F

        setIcon(icon)
        setDescription(descText)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val iconIntrWidth = if (icon != null) icon!!.intrinsicWidth else 0
        val iconIntrHeight = if (icon != null) icon!!.intrinsicHeight else 0
        val longestText = longestTextWidth()
        val cWidth =
            longestText + paddingRight + paddingStart + paddingTop + paddingLeft + iconIntrWidth
        val cHeight =
            ViewResources.textHeight(paint) + if (descText != null) ViewResources.textHeight(paint) else 0 + paddingTop + paddingBottom + iconIntrHeight
        val minWidth = measuredWidth.coerceAtLeast(cWidth)
        val minHeight = measuredHeight.coerceAtLeast(cHeight) + textSize.toInt()
        setMeasuredDimension(minWidth, minHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.getTextBounds(text.toString(), 0, text.length, textBound)
        val textY = textSize + textBound.bottom + paddingTop
        val textX = (icon?.intrinsicWidth ?: 0).toFloat() + paddingStart

        if (descText != null) {
            canvas?.let {
                if (descText != null) {
                    ViewResources.drawTextCenter(
                        canvas,
                        descTextPaint,
                        textBound,
                        descText ?: "",
                        0F,
                        textY
                    )
                }
            }
        }
    }

    fun setDescription(
        text: String?,
        textSizeFloat: Float? = null,
        @ColorRes textColorDrawable: Int? = null
    ) {
        text?.let { txt ->
            descText = txt
        }
        textSizeFloat?.let { size ->
            descTextSize = size
        }

        textColorDrawable.let { color ->
            descTextColor = color
        }
        postInvalidate()
    }

    /**
     * Sets the icon to show for this button. By default, this icon will be shown on the left side of
     * the button.
     *
     * @param icon Drawable to use for the button's icon.
     * @attr ref com.google.android.material.R.styleable#MaterialButton_icon
     * @see .setIconResource
     * @see .getIcon
     */
    fun setIcon(icon: Drawable?) {
        icon?.apply {
            setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
            setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
        }
        postInvalidate()
    }

    private fun longestTextWidth(): Int {
        val buttonTextSize = paint.measureText(text.toString()).toInt()
        val descTextSize = descText?.let { paint.measureText(descText).toInt() } ?: 0
        return buttonTextSize.coerceAtLeast(descTextSize)
    }

    override fun performClick(): Boolean {
        super.performClick()
        Log.d(javaClass.simpleName, "A click event")
        invalidate()
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        // Listening for the down and up touch events
        return when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownTouch = true
                Log.d(javaClass.simpleName, "A touch event: A_DOWN")
                background = ResourcesCompat.getDrawable(
                    resources,
                    R.color.design_default_color_background,
                    null
                )
                true
            }
            MotionEvent.ACTION_UP -> {
                if (mDownTouch) {
                    mDownTouch = false
                    Log.d(javaClass.simpleName, "A touch event: A_UP")
                    background = currentBg
                    performClick() // Call this method to handle the response, and
                } else {
                    false
                }
            }
            else -> false
        }
    }
}