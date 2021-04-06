package com.buchi.buttoner.buttons

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.buchi.buttoner.R

class AwesomeCustomView constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.button_compound_view, this)
        val image = getViewById(R.id.image) as ImageView
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.AwesomeCustomView)
            val imageTintColorResource = typedArray.getResourceId(
                R.styleable.AwesomeCustomView_awesomeImageTintColor,
                android.R.color.white
            )
            val imageTintColor = ContextCompat.getColor(context, imageTintColorResource)
            ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(imageTintColor))

            typedArray.recycle()
        }
    }
}