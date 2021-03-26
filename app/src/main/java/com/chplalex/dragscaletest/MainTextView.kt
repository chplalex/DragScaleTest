package com.chplalex.dragscaletest

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet

class MainTextView: androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            val str1 = "l = $left t = $top r = $right b = $bottom"
            val str2 = "x = $x, y = $y, w = $width, h = $height"
            val str3 = "tX = $translationX tY = $translationY"
            val str4 = "sX = $scaleX sY = $scaleY"
            val str5 = "pX = $pivotX pY = $pivotY"
            text = String.format("%s\n%s\n%s\n%s\n%s", str1, str2, str3, str4, str5)
        }
    }
}