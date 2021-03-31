package com.buchi.buttoner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class TestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val rectSquare = Rect()
    private val paintSquare = Paint(Paint.ANTI_ALIAS_FLAG)
    private val SQAURE_SIZE_DEF = 200
    private var squareColor: Int = 0
    private var squareSize = SQAURE_SIZE_DEF

    private val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        // Position and size
        paintSquare.color = Color.GREEN

        attrs?.let { set->
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomTestView,
                0, 0).apply {

                try {
                    squareColor = getColor(R.styleable.CustomTestView_square_color, Color.GREEN)
                    squareSize = getDimensionPixelSize(R.styleable.CustomTestView_square_size, SQAURE_SIZE_DEF)
                    paintSquare.color = squareColor

                    paintCircle.color = Color.parseColor("#FF03DAC5")

                } finally {
                    recycle()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        // Draw background
//        canvas?.drawColor(Color.RED)

        // Draw object
        // 1. Make a rect object to draw pixel positions
        rectSquare.left = 50
        rectSquare.top = 50
        rectSquare.right = rectSquare.left + squareSize
        rectSquare.bottom = rectSquare.bottom + squareSize

        // Needs and instance of rect & paint
        canvas?.drawRect(rectSquare, paintSquare)

        val radius = 200f
        val cx = width - radius - 50f
        val cy: Float = rectSquare.top.toFloat() + (rectSquare.height() / 2)
        canvas?.drawCircle(cx, cy, radius, paintCircle)
    }

    fun swapColor() {
        paintSquare.color = if (paintSquare.color == squareColor) Color.RED else squareColor
        postInvalidate()
    }

}