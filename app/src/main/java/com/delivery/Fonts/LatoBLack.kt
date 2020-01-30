package com.delivery.Fonts

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.delivery.Fonts.FontCacheRagular.getTypeface

@SuppressLint("AppCompatCustomView")
class LatoBLack : TextView {
    constructor(context: Context) : super(context) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        applyCustomFont(context)
    }

    private fun applyCustomFont(context: Context) {
        val customFont = getTypeface("Bold.otf", context)
        typeface = customFont
    }
}