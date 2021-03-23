package com.chplalex.dragscaletest

import android.content.Context
import android.util.AttributeSet

class MainTextView: androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            val str = "l = $left t = $top\nr = $right b = $bottom\ntX = $translationX tY = $translationY"
            text = str
        }
    }
}