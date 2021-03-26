package com.chplalex.dragscaletest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var pointView: PointView

    private val commonGestureListener = CommonGestureListener()
    private val scaleGestureListener = ScaleGestureListener()

    private lateinit var commonGestureDetector : GestureDetector
    private lateinit var scaleGestureDetector : ScaleGestureDetector

    private var scaleFactor = 1f

    inner class CommonGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            contentContainer.translationX -= distanceX
            contentContainer.translationY -= distanceY
            return true
        }
    }

    inner class ScaleGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
                return if (detector.isInProgress) {
                    scaleFactor *= detector.scaleFactor
                    scaleFactor = max(MIN_SCALE, min(scaleFactor, MAX_SCALE))
                    contentContainer.scaleX = scaleFactor
                    contentContainer.scaleY = scaleFactor
                    true
                } else {
                    false
                }
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
        pointView = createPointView()
    }

    private fun createPointView() = findViewById<PointView>(R.id.point_view)

    private fun createView(id: Int) = findViewById<MainTextView>(id)

    private fun initGestureDetectors() {
        commonGestureDetector = GestureDetector(this, commonGestureListener)
        scaleGestureDetector = ScaleGestureDetector(this, scaleGestureListener)
    }
}