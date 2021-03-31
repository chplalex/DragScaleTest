package com.chplalex.dragscaletest

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.max
import kotlin.math.min

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

    private val pathPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = LINE_HEIGHT
        color = lineColor
    }

    private val touchableRectPaint = Paint().apply {
        alpha = 0
    }

    private val touchableRect = Rect()

    private val startX = segment.startCoordinates.x
    private val startY = segment.startCoordinates.y
    private val endX = segment.endCoordinates.x
    private val endY = segment.endCoordinates.y

    init {
        path.moveTo(
            segment.startCoordinates.x.toPx(),
            segment.startCoordinates.y.toPx()
        )
        path.cubicTo(
            segment.bezieFirstPoint.x.toPx(),
            segment.bezieFirstPoint.y.toPx(),
            segment.bezieSecondPoint.x.toPx(),
            segment.bezieSecondPoint.y.toPx(),
            segment.endCoordinates.x.toPx(),
            segment.endCoordinates.y.toPx()
        )

        val minX = with(segment) { min(startCoordinates.x, endCoordinates.x) }
        val minY = with(segment) { min(startCoordinates.y, endCoordinates.y) }

        val maxX = with(segment) { max(startCoordinates.x, endCoordinates.x) }
        val maxY = with(segment) { max(startCoordinates.y, endCoordinates.y) }

        val dX = maxX - minX
        val dY = maxY - minY

        val centerX = minX + dX / 2
        val centerY = minY + dY / 2

        val dd = max(dX, dY) / 6

        touchableRect.set(
            (centerX - dd).toPx().toInt(),
            (centerY - dd).toPx().toInt(),
            (centerX + dd).toPx().toInt(),
            (centerY + dd).toPx().toInt()
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(path, pathPaint)
        canvas?.drawRect(touchableRect, touchableRectPaint)
    }

    fun dpToPx(context: Context, dp: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )

    override fun onTouchEvent(event: MotionEvent) =
        if (touchableRect.contains(event.x.toInt(), event.y.toInt())) {
            super.onTouchEvent(event)
        } else {
            false
        }

    private fun min4(i1: Int, i2: Int, i3: Int, i4: Int) = min(min(i1, i2), min(i3, i4))

    private fun max4(i1: Int, i2: Int, i3: Int, i4: Int) = max(max(i1, i2), max(i3, i4))

    private fun Int.toPx() = dpToPx(context, this.toFloat())

}