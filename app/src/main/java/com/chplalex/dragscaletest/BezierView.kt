package com.chplalex.dragscaletest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

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
    private val paint = Paint()

    init {
        paint.apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = LINE_HEIGHT
            color = lineColor
        }

        val dX = with(segment) {
            min(
                min(startCoordinates.x, endCoordinates.x),
                min(bezieFirstPoint.x, bezieSecondPoint.x)
            )
        }

        val dY = with(segment) {
            min(
                min(startCoordinates.y, endCoordinates.y),
                min(bezieFirstPoint.y, bezieSecondPoint.y)
            )
        }

        path.moveTo(
            dpToPx(context, segment.startCoordinates.x.toFloat()),
            dpToPx(context, segment.startCoordinates.y.toFloat())
        )
        path.cubicTo(
            dpToPx(context, segment.bezieFirstPoint.x.toFloat()),
            dpToPx(context, segment.bezieFirstPoint.y.toFloat()),
            dpToPx(context, segment.bezieSecondPoint.x.toFloat()),
            dpToPx(context, segment.bezieSecondPoint.y.toFloat()),
            dpToPx(context, segment.endCoordinates.x.toFloat()),
            dpToPx(context, segment.endCoordinates.y.toFloat())
        )
        path.offset(
            dpToPx(context, LINE_HEIGHT / 2 - dX.toFloat()),
            dpToPx(context, LINE_HEIGHT / 2 - dY.toFloat())
        )

        translationX = dpToPx(context, dX.toFloat() - LINE_HEIGHT / 2)
        translationY = dpToPx(context, dY.toFloat() - LINE_HEIGHT / 2)

        val rect = RectF()
        path.computeBounds(rect, true)
        layoutParams = ViewGroup.LayoutParams(
            max(LINE_HEIGHT, rect.width()).toInt(),
            max(LINE_HEIGHT, rect.height()).toInt()
        )

        var backgroundColor = Random.nextInt()
        if (backgroundColor == lineColor) {
            backgroundColor = Random.nextInt()
        }
        setBackgroundColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(path, paint)
    }

    fun dpToPx(context: Context, dp: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )

}