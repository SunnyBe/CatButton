package com.buchi.buttoner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.buchi.buttoner.utils.Utils

const val DESC_TEXT_SIZE = 50F
const val DESC_ICON_SIZE = 100F
const val TEXT_X = "textX"
const val TEXT_Y = "textY"

class DynamicButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // Button text params
    private var buttonText: String
    private var buttonTextX: Float = 10f
    private var buttonTextY: Float = 10f
    private var buttonTextSize: Float = 16f
    private var buttonTextColor: Int = Color.BLACK
    private var buttonTextRec = Rect()
    private var buttonTextPaint = Paint().apply {
        isAntiAlias = true
    }

    // Description text
    private var descText: String? = null
    private var descTextX: Float = 10f
    private var descTextY: Float = 10f
    private var descTextSize: Float = 16f
    private var descTextColor: Int = Color.BLACK
    private var descTextRec = Rect()
    private var descTextPaint = Paint().apply {
        isAntiAlias = true
    }

    private var icon: Int = 0
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

        buttonText = typedArray.getString(R.styleable.DynamicButton_button_text) ?: "Button"
        buttonTextSize = typedArray.getDimension(R.styleable.DynamicButton_textSize, 16f)
        buttonTextColor = typedArray.getColor(R.styleable.DynamicButton_textColor, Color.BLACK)

        descText = typedArray.getString(R.styleable.DynamicButton_desc_text)
        descTextSize = typedArray.getDimension(R.styleable.DynamicButton_desc_text_size, 14f)

        icon = typedArray.getResourceId(R.styleable.DynamicButton_desc_icon, 0)

//        icon = R.drawable.ic_share_black // Todo Remove line
        iconBitmap = if (icon != 0) Utils.convertIconToBitmap(context, icon) else null

        buttonTextPaint.apply {
            color = buttonTextColor
            textSize = buttonTextSize
        }

        descTextPaint.apply {
            color = descTextColor
            textSize = descTextSize
        }

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        setMeasuredDimension(
            measureDimension(desiredWidth, widthMeasureSpec),
            measureDimension(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        // Draw the button text
        val buttonTextPosition = drawTextCenter(canvas, buttonTextPaint, buttonText)
        buttonTextX = buttonTextPosition[TEXT_X] ?: 0f
        buttonTextY = buttonTextPosition[TEXT_Y] ?: 0f

//        descText = "Subtitle Text"  // Todo Remove line
        descText?.let { text ->
            descTextX = buttonTextX
            descTextY = buttonTextY + buttonTextSize
            val descTextPosition = drawTextCenter(canvas, descTextPaint, text, buttonTextSize)
            buttonTextX = descTextPosition[TEXT_X] ?: 0f
            buttonTextY = descTextPosition[TEXT_Y] ?: 0f
        }
        iconBitmap?.let { bitmap->
            canvas.drawBitmap(bitmap, iconStartMargin, height / 2f, iconPaint)
        }

    }

    private fun drawTextCenter(canvas: Canvas, paint: Paint, text: String): HashMap<String, Float> {
        val positionHashMap = HashMap<String, Float>()
        canvas.getClipBounds(buttonTextRec)
        val cHeight: Int = buttonTextRec.height()
        val cWidth: Int = buttonTextRec.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(text, 0, text.length, buttonTextRec)
        val x: Float = cWidth / 2f - buttonTextRec.width() / 2f - buttonTextRec.left
        val y: Float = cHeight / 2f + buttonTextRec.height() / 2f - buttonTextRec.bottom
        canvas.drawText(text, x, y, paint)
        positionHashMap[TEXT_X] = x
        positionHashMap[TEXT_Y] = y
        return positionHashMap
    }

    private fun drawTextCenter(canvas: Canvas, paint: Paint, text: String, extendY: Float): HashMap<String, Float> {
        val positionHashMap = HashMap<String, Float>()
        canvas.getClipBounds(buttonTextRec)
        val cHeight: Int = buttonTextRec.height()
        val cWidth: Int = buttonTextRec.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(text, 0, text.length, buttonTextRec)
        val x: Float = cWidth / 2f - buttonTextRec.width() / 2f - buttonTextRec.left
        val y: Float = (cHeight / 2f + buttonTextRec.height() / 2f - buttonTextRec.bottom) + extendY
        canvas.drawText(text, x, y, paint)
        positionHashMap[TEXT_X] = x
        positionHashMap[TEXT_Y] = y
        return positionHashMap
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        if (result < desiredSize) {
            Log.e(javaClass.simpleName, "The view is too small, the content might get cut")
        }
        return result
    }

}