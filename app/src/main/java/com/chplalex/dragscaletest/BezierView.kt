package com.chplalex.dragscaletest

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

private const val LINE_HEIGHT = 48F

class BezierView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    private val lineColor: Int,
    segment: MetroLineSegmentUiModel
) : View(context, attrs, defStyleAttr) {

    private val path = Path()

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = LINE_HEIGHT
        color = lineColor
    }

    init {
        path.moveTo(
            segment.startCoordinates.x.toPx(),
            segment.startCoordinates.y.toPx()
        )
        path.cubicTo(
            segment.bezierFirstPoint.x.toPx(),
            segment.bezierFirstPoint.y.toPx(),
            segment.bezierSecondPoint.x.toPx(),
            segment.bezierSecondPoint.y.toPx(),
            segment.endCoordinates.x.toPx(),
            segment.endCoordinates.y.toPx()
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent) = false

    private fun Int.toPx() = dpToPx(context, this.toFloat())
}