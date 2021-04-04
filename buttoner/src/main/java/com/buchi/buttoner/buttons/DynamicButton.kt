package com.buchi.buttoner.buttons

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.Px
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import com.buchi.buttoner.R
import com.buchi.buttoner.resources.ViewResources
import com.buchi.buttoner.utils.ViewUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButton.IconGravity


/**
 * Gravity used to position the icon at the start of the view.
 *
 * @see .setIconGravity
 * @see .getIconGravity
 */
const val ICON_GRAVITY_START = 0x1

/**
 * Gravity used to position the icon in the center of the view at the start of the text
 *
 * @see .setIconGravity
 * @see .getIconGravity
 */
const val ICON_GRAVITY_TEXT_START = 0x2

/**
 * Gravity used to position the icon at the end of the view.
 *
 * @see .setIconGravity
 * @see .getIconGravity
 */
const val ICON_GRAVITY_END = 0x3

/**
 * Gravity used to position the icon in the center of the view at the end of the text
 *
 * @see .setIconGravity
 * @see .getIconGravity
 */
const val ICON_GRAVITY_TEXT_END = 0x4

/**
 * Gravity used to position the icon at the top of the view.
 *
 * @see .setIconGravity
 * @see .getIconGravity
 */
const val ICON_GRAVITY_TOP = 0x10

/**
 * Gravity used to position the icon in the center of the view at the top of the text
 *
 * @see .setIconGravity
 * @see .getIconGravity
 */
const val ICON_GRAVITY_TEXT_TOP = 0x20

/** Positions the icon can be set to.  */
@IntDef(
    ICON_GRAVITY_START,
    ICON_GRAVITY_TEXT_START,
    ICON_GRAVITY_END,
    ICON_GRAVITY_TEXT_END,
    ICON_GRAVITY_TOP,
    ICON_GRAVITY_TEXT_TOP
)
@Retention(AnnotationRetention.SOURCE)
annotation class IconGravity

class DynamicButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {
    private val TEXT_X = "textX"
    private val TEXT_Y = "textY"
    private val minButtonWidth = 350
    private val minButtonHeight = 100

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

    // Icon desc
    @IconGravity
    private val iconGravity = 0
    private var iconTintMode: PorterDuff.Mode? = null
    private var iconTint: ColorStateList? = null
    private var icon: Drawable?

    @Px
    private var iconSize: Int = 0

    @Px
    private var iconLeft: Int = 0

    @Px
    private var iconTop: Int = 0

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

        buttonText = "Button"
        buttonTextSize = typedArray.getDimension(R.styleable.DynamicButton_textSize, 16f)
        buttonTextColor = typedArray.getColor(R.styleable.DynamicButton_textColor, Color.BLACK)

        descText = typedArray.getString(R.styleable.DynamicButton_desc_text)
        descTextSize = typedArray.getDimension(R.styleable.DynamicButton_desc_text_size, 14f)

        icon = ViewResources.getDrawable(context, typedArray, R.styleable.DynamicButton_icon)
        iconTintMode = ViewUtils.parseTintMode(
            typedArray.getInt(R.styleable.DynamicButton_iconTintMode, -1),
            PorterDuff.Mode.SRC_IN
        )
        iconTint = ViewResources.getColorStateList(
            getContext(),
            typedArray,
            R.styleable.DynamicButton_iconTint
        )

        buttonTextPaint.apply {
            color = buttonTextColor
            textSize = buttonTextSize
        }

        descTextPaint.apply {
            color = descTextColor
            textSize = descTextSize
        }

        updateIcon(icon != null)
        setIcon(icon)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = minButtonWidth + paddingLeft + paddingRight
        val desiredHeight = minButtonHeight + paddingTop + paddingBottom

        setMeasuredDimension(
            measureDimension(desiredWidth, widthMeasureSpec),
            measureDimension(desiredHeight, heightMeasureSpec)
        )
    }

//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
//        // Workaround for API 21 ripple bug (possibly internal in GradientDrawable)
//        if (VERSION.SDK_INT == VERSION_CODES.LOLLIPOP && materialButtonHelper != null) {
//            materialButtonHelper.updateMaskBounds(bottom - top, right - left)
//        }
//    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateIconPosition(w, h)
    }

    override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
        super.onTextChanged(charSequence, i, i1, i2)
        updateIconPosition(measuredWidth, measuredHeight)
    }

//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//        if (canvas == null) return
//        // Draw the button text
//        val buttonTextPosition = drawTextCenter(canvas, buttonTextPaint, buttonText)
//        buttonTextX = buttonTextPosition[TEXT_X] ?: 0f
//        buttonTextY = buttonTextPosition[TEXT_Y] ?: 0f
//
////        descText = "Subtitle Text"  // Todo Remove line
//        descText?.let { text ->
//            descTextX = buttonTextX
//            descTextY = buttonTextY + buttonTextSize
//            val descTextPosition = drawTextCenter(canvas, descTextPaint, text, buttonTextSize)
//            buttonTextX = descTextPosition[TEXT_X] ?: 0f
//            buttonTextY = descTextPosition[TEXT_Y] ?: 0f
//        }
//        iconBitmap?.let { bitmap ->
//            canvas.drawBitmap(bitmap, iconStartMargin, height / 2f, iconPaint)
//        }
//
//    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val descText = "Desc Text"
        val textWidth = getTextWidth()
        val textHeight = getTextHeight()
        canvas?.drawText(descText, textWidth.toFloat(), textHeight+20F, descTextPaint)
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

    private fun drawTextCenter(
        canvas: Canvas,
        paint: Paint,
        text: String,
        extendY: Float
    ): HashMap<String, Float> {
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

    /**
     * Updates the icon, icon tint, and icon tint mode for this button.
     * @param needsIconReset Whether to force the drawable to be set
     */
    private fun updateIcon(needsIconReset: Boolean) {
        if (icon != null) {
            icon = DrawableCompat.wrap(icon!!).mutate()
            DrawableCompat.setTintList(icon!!, iconTint)
            iconTintMode?.let {
                DrawableCompat.setTintMode(icon!!, iconTintMode!!)
            }
            val width = if (iconSize != 0) iconSize else icon!!.intrinsicWidth
            val height = if (iconSize != 0) iconSize else icon!!.intrinsicHeight
            icon?.setBounds(iconLeft, iconTop, iconLeft + width, iconTop + height)
            setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
            setPadding(0, 0, 0, 0)
        }

        // Forced icon update
        if (needsIconReset) {
            resetIconDrawable()
            return
        }
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
//        if (this.icon !== icon) {
//            this.icon = icon
//            updateIcon( /* needsIconReset = */true)
//            updateIconPosition(measuredWidth, measuredHeight)
//        }
        icon?.apply {
//            setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
//            setPadding(0, 0, 0, 0)
            updateIcon(true)
        }
    }

    fun setIcon(@DrawableRes icon: Int) {
        val drawable = ResourcesCompat.getDrawable(resources, icon, null)
        drawable?.apply {
            val padding = width / 2 - drawable.intrinsicWidth / 2
            setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
            setPadding(padding, 0, 0, 0)
        }
    }

    /**
     * Sets the icon drawable resource to show for this button. By default, this icon will be shown on
     * the left side of the button.
     *
     * @param iconResourceId Drawable resource ID to use for the button's icon.
     * @attr ref com.google.android.material.R.styleable#MaterialButton_icon
     * @see .setIcon
     * @see .getIcon
     */
    fun setIconResource(@DrawableRes iconResourceId: Int) {
        var icon: Drawable? = null
        if (iconResourceId != 0) {
            icon = AppCompatResources.getDrawable(context, iconResourceId)
        }
        setIcon(icon)
    }

    /**
     * Sets the tint list for the icon shown for this button.
     *
     * @param iconTint Tint list for the icon shown for this button.
     * @attr ref com.google.android.material.R.styleable#MaterialButton_iconTint
     * @see .setIconTintResource
     * @see .getIconTint
     */
    fun setIconTint(iconTint: ColorStateList?) {
        if (this.iconTint !== iconTint) {
            this.iconTint = iconTint
            updateIcon( /* needsIconReset = */false)
        }
    }

    /**
     * Sets the tint list color resource for the icon shown for this button.
     *
     * @param iconTintResourceId Tint list color resource for the icon shown for this button.
     * @attr ref com.google.android.material.R.styleable#MaterialButton_iconTint
     * @see .setIconTint
     * @see .getIconTint
     */
    fun setIconTintResource(@ColorRes iconTintResourceId: Int) {
        setIconTint(AppCompatResources.getColorStateList(context, iconTintResourceId))
    }

    private fun resetIconDrawable() {
        if (isIconStart()) {
            TextViewCompat.setCompoundDrawablesRelative(this, icon, null, null, null)
        } else if (isIconEnd()) {
            TextViewCompat.setCompoundDrawablesRelative(this, null, null, icon, null)
        } else if (isIconTop()) {
            TextViewCompat.setCompoundDrawablesRelative(this, null, icon, null, null)
        }
    }

    private fun isIconStart(): Boolean {
        return iconGravity == ICON_GRAVITY_START || iconGravity == ICON_GRAVITY_TEXT_START
    }

    private fun isIconEnd(): Boolean {
        return iconGravity == ICON_GRAVITY_END || iconGravity == ICON_GRAVITY_TEXT_END
    }

    private fun isIconTop(): Boolean {
        return iconGravity == ICON_GRAVITY_TOP || iconGravity == MaterialButton.ICON_GRAVITY_TEXT_TOP
    }

    private fun updateIconPosition(buttonWidth: Int, buttonHeight: Int) {
        if (icon == null || layout == null) {
            return
        }
        if (isIconStart() || isIconEnd()) {
            iconTop = 0
            if (iconGravity == ICON_GRAVITY_START || iconGravity == ICON_GRAVITY_END) {
                iconLeft = 0
                updateIcon( /* needsIconReset = */false)
                return
            }
            val localIconSize = if (iconSize == 0) icon!!.intrinsicWidth else iconSize
            var newIconLeft = ((buttonWidth
                    - getTextWidth()
                    - ViewCompat.getPaddingEnd(this)
                    - localIconSize
                    - iconPadding
                    - ViewCompat.getPaddingStart(this))
                    / 2)

            // Only flip the bound value if either isLayoutRTL() or iconGravity is textEnd, but not both
            if (isLayoutRTL() != (iconGravity == ICON_GRAVITY_TEXT_END)) {
                newIconLeft = -newIconLeft
            }
            if (iconLeft != newIconLeft) {
                iconLeft = newIconLeft
                updateIcon( /* needsIconReset = */false)
            }
        } else if (isIconTop()) {
            iconLeft = 0
            if (iconGravity == ICON_GRAVITY_TOP) {
                iconTop = 0
                updateIcon( /* needsIconReset = */false)
                return
            }
            val localIconSize = if (iconSize == 0) icon!!.intrinsicHeight else iconSize
            val newIconTop = ((buttonHeight
                    - getTextHeight()
                    - paddingTop
                    - localIconSize
                    - iconPadding
                    - paddingBottom)
                    / 2)
            if (iconTop != newIconTop) {
                iconTop = newIconTop
                updateIcon( /* needsIconReset = */false)
            }
        }
    }

    private fun getTextWidth(): Int {
        val textPaint: Paint = paint
        var buttonText = text.toString()
        if (transformationMethod != null) {
            // if text is transformed, add that transformation to to ensure correct calculation
            // of icon padding.
            buttonText = transformationMethod.getTransformation(buttonText, this).toString()
        }
        return Math.min(textPaint.measureText(buttonText).toInt(), layout.ellipsizedWidth)
    }

    private fun getTextHeight(): Int {
        val textPaint: Paint = paint
        var buttonText = text.toString()
        if (transformationMethod != null) {
            // if text is transformed, add that transformation to to ensure correct calculation
            // of icon padding.
            buttonText = transformationMethod.getTransformation(buttonText, this).toString()
        }
        val bounds = Rect()
        textPaint.getTextBounds(buttonText, 0, buttonText.length, bounds)
        return Math.min(bounds.height(), layout.height)
    }

    private fun isLayoutRTL(): Boolean {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
    }

}