package com.chplalex.dragscaletest

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.TextView
import kotlin.math.max
import kotlin.math.min

private const val MIN_SCALE = 0.1f
private const val MAX_SCALE = 2.0f

class MainActivity : AppCompatActivity() {

    private lateinit var mainLayout: FrameLayout
    private lateinit var contentContainer: FrameLayout

    private lateinit var view01: TextView
    private lateinit var view02: TextView
    private lateinit var view03: TextView
    private lateinit var view04: TextView
    private lateinit var pointPivot: PointView
    private lateinit var pointStart: PointView
    private lateinit var pointEnd: PointView
    private lateinit var segmentView: View

    private val commonGestureListener = CommonGestureListener()
    private val scaleGestureListener = ScaleGestureListener()

    private lateinit var commonGestureDetector : GestureDetector
    private lateinit var scaleGestureDetector : ScaleGestureDetector

    private val startX = 100
    private val startY = 150
    private val endX = 300
    private val endY = 450

    private val segment = MetroLineSegmentUiModel(
        startCoordinates = MetroPointUiModel(
            x = startX,
            y = startY
        ),
        endCoordinates = MetroPointUiModel(
            x = endX,
            y = endY
        ),
        bezieFirstPoint = MetroPointUiModel(
            x = startX,
            y = startY
        ),
        bezieSecondPoint = MetroPointUiModel(
            x = endX,
            y = endY
        ),
        isFinished = true
    )

    private var scaleFactor = 1f

    inner class CommonGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            return if (scaleGestureDetector.isInProgress) {
                false
            } else {
                contentContainer.translationX -= distanceX
                contentContainer.translationY -= distanceY
                true
            }
        }
    }

    inner class ScaleGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
                return if (detector.isInProgress) {
                    with(detector) { pointPivot.showAt(focusX, focusY) }
                    scaleFactor *= detector.scaleFactor
                    scaleFactor = max(MIN_SCALE, min(scaleFactor, MAX_SCALE))
                    contentContainer.scaleX = scaleFactor
                    contentContainer.scaleY = scaleFactor
                    true
                } else {
                    false
                }
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            super.onScaleEnd(detector)
            pointPivot.makeGone()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initLayouts()
        initViews()
        initGestureDetectors()
    }

    private fun initLayouts() {
        mainLayout = findViewById(R.id.main_layout)
        contentContainer = layoutInflater.inflate(R.layout.content_container, mainLayout) as FrameLayout
        contentContainer.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, Gravity.CENTER)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return commonGestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private fun initViews() {
        view01 = createView(R.id.view01)
        view02 = createView(R.id.view02)
        view03 = createView(R.id.view03)
        view04 = createView(R.id.view04)
        pointPivot = createPointPivot()
//        pointStart = createPointStart()
//        pointEnd = createPointEnd()
        segmentView = createSegmentView(segment, Color.RED)
        contentContainer.addView(segmentView)
    }

//    private fun createPointStart() = findViewById<PointView>(R.id.point_start).also {
//        it.showAt(startX.toPx(), startY.toPx())
//    }
//
//    private fun createPointEnd() = findViewById<PointView>(R.id.point_end).also {
//        it.showAt(endX.toPx(), endY.toPx())
//    }

    private fun createPointPivot() = findViewById<PointView>(R.id.point_pivot)

    private fun createSegmentView(segment: MetroLineSegmentUiModel, lineColor: Int): View {
//        return RectView(context = this, lineColor = lineColor, segment = segment)
        return BezierView(context = this, lineColor = lineColor, segment = segment)
    }

    private fun createView(id: Int) = findViewById<MainTextView>(id)

    private fun initGestureDetectors() {
        commonGestureDetector = GestureDetector(this, commonGestureListener)
        scaleGestureDetector = ScaleGestureDetector(this, scaleGestureListener)
    }

    private fun View.showAt(atX: Float, atY: Float) {
        this.translationX = 0f
        this.translationY = 0f
//        this.scaleX = 0f
//        this.scaleY = 0f
        this.pivotX = 0f
        this.pivotY = 0f
        this.x = atX
        this.y = atY
        this.visibility = View.VISIBLE
    }

    private fun View.makeGone() {
        this.visibility = View.GONE
    }

    fun dpToPx(context: Context, dp: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )

    private fun Int.toPx() = dpToPx(this@MainActivity, this.toFloat())
}