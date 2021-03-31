package com.chplalex.dragscaletest

import android.content.Context
import android.graphics.*
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import kotlin.math.*
import kotlin.random.Random

private const val LINE_HEIGHT = 24f

private const val RECT_HALF_WIDTH = 48f

class RectView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    private val lineColor: Int,
    segment: MetroLineSegmentUiModel
) : View(context, attrs, defStyleAttr) {

    private val path = Path()

    private val rect = Rect()

    private val paintPath = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = LINE_HEIGHT
        color = lineColor
    }

    private val paint2 = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val startX = segment.startCoordinates.x
    private val startY = segment.startCoordinates.y
    private val endX = segment.endCoordinates.x
    private val endY = segment.endCoordinates.y

    init {

        val dX = abs(startX - endX)
        val dY = abs(startY - endY)
        val angle = atan2(dY.toDouble(), dX.toDouble())
        showMsg("angle = $angle")
        val ddX = RECT_HALF_WIDTH * sin(angle)
        val ddY = RECT_HALF_WIDTH * cos(angle)

        val ovalShape = OvalShape()

//        val dX = with(segment) {
//            min4(startCoordinates.x, endCoordinates.x, bezieFirstPoint.x, bezieSecondPoint.x)
//        }
//
//        val dY = with(segment) {
//            min4(startCoordinates.y, endCoordinates.y, bezieFirstPoint.y, bezieSecondPoint.y)
//        }

        path.moveTo(
            segment.startCoordinates.x.toPx(),
            segment.startCoordinates.y.toPx()
        )
        path.lineTo(
            segment.endCoordinates.x.toPx(),
            segment.endCoordinates.y.toPx()
        )

//        translationX = dpToPx(context, dX.toFloat() - LINE_HEIGHT / 2)
//        translationY = dpToPx(context, dY.toFloat() - LINE_HEIGHT / 2)

//        path.offset(
//            dpToPx(context, (LINE_HEIGHT / 4) - dX.toFloat()),
//            dpToPx(context, (LINE_HEIGHT / 4) - dY.toFloat())
//        )


//        val rect = RectF()
//        path.computeBounds(rect, true)
//        layoutParams = ViewGroup.LayoutParams(
//            max(LINE_HEIGHT, rect.width()).toInt(),
//            max(LINE_HEIGHT, rect.height()).toInt()
//        )

        var backgroundColor = Random.nextInt()
        if (backgroundColor == lineColor) {
            backgroundColor = Random.nextInt()
        }
        setBackgroundColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(path, paintPath)

        canvas?.drawCircle(startX.toPx(), startY.toPx(), 8f, paint2)
        canvas?.drawCircle(endX.toPx(), endY.toPx(), 8f, paint2)
    }

    fun dpToPx(context: Context, dp: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )

    private fun min4(i1: Int, i2: Int, i3: Int, i4: Int) = min(min(i1, i2), min(i3, i4))

    private fun max4(i1: Int, i2: Int, i3: Int, i4: Int) = max(max(i1, i2), max(i3, i4))

    private fun Int.toPx() = dpToPx(context, this.toFloat())

    private fun showMsg(msg: String) {
        Log.d("CHPL", msg)
    }

}