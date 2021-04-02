package com.chplalex.dragscaletest

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.math.max
import kotlin.math.min

private const val MIN_SCALE = 1f
private const val MAX_SCALE = 2f
private const val NORM_SCALE = 1f
private const val SCALE_DURATION = 500L

class MainActivity : AppCompatActivity() {

    private lateinit var mainLayout: FrameLayout
    private lateinit var contentContainer: FrameLayout

    private lateinit var view01: TextView
    private lateinit var view02: TextView
    private lateinit var view03: TextView
    private lateinit var view04: TextView
    private lateinit var pointPivot: PointView
    private lateinit var segmentView: View
    private lateinit var centerView: ImageView

    private val commonGestureListener = CommonGestureListener()
    private val scaleGestureListener = ScaleGestureListener()

    private lateinit var commonGestureDetector: GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector

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
        bezierFirstPoint = MetroPointUiModel(
            x = startX,
            y = startY
        ),
        bezierSecondPoint = MetroPointUiModel(
            x = endX,
            y = endY
        ),
        isFinished = true
    )

    private var scaleFactor = 1f

    inner class CommonGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return if (scaleGestureDetector.isInProgress) {
                false
            } else {
                contentContainer.translationX -= distanceX
                contentContainer.translationY -= distanceY
                true
            }
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            val bounds = Rect()
            val offset = Point()
            contentContainer.getGlobalVisibleRect(bounds, offset)

            return if (bounds.contains(e.x.toInt(), e.y.toInt())) {

                val pX = (e.x - offset.x) / scaleFactor
                val pY = (e.y - offset.y) / scaleFactor

                pointPivot.showAt(pX, pY)

                showBefore()

                if (contentContainer.hasScaleNearToMin()) {
                    doAnimation(MAX_SCALE, pX, pY)
                } else {
                    doAnimation(MIN_SCALE, pX, pY)
                }
                true
            } else {
                false
            }
        }
    }

    inner class ScaleGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            return if (detector.isInProgress) {
                // with(detector) { pointPivot.showAt(focusX, focusY) }
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
//            pointPivot.makeGone()
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
        contentContainer = findViewById(R.id.content_container)
        contentContainer.pivotX = 0f
        contentContainer.pivotY = 0f
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
        segmentView = createSegmentView(segment, Color.RED)
        segmentView.visibility = View.INVISIBLE
        segmentView.setOnClickListener {
            Toast.makeText(this, "segment clicked", Toast.LENGTH_SHORT).show()
        }
        contentContainer.addView(segmentView)

        centerView = findViewById(R.id.metro_map_center_focus_icon)
        centerView.setOnClickListener { resetAnimation() }
    }

    private fun showMsg(msg: String) {
        Log.d("CHPL", msg)
    }

    private val animatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
            showMsg("animation start")
            showStatus()
            // nothing
        }

        override fun onAnimationEnd(animation: Animator?) {
            showMsg("animation end")
            showStatus()
            scaleFactor = contentContainer.scaleX
        }

        override fun onAnimationCancel(animation: Animator?) {
            scaleFactor = contentContainer.scaleX
        }

        override fun onAnimationRepeat(animation: Animator?) {
            // nothing
        }
    }

    private fun showStatus() {
        with (contentContainer) {
            showMsg("x = $x y = $y w = $width h = $height")
            showMsg("pivotX = $pivotX pivotY = $pivotY")
            showMsg("scaleX = $scaleX scaleY = $scaleY")
            showMsg("translationX = $translationX translationY = $translationY")
        }
    }

    private fun doAnimation(scaleTarget: Float, pX: Float, pY: Float) {
        val dX = with (contentContainer) { pX * (scaleTarget - 1) }
        val dY = with (contentContainer) { pY * (scaleTarget - 1) }

        val animatorTranslationX = createPropertyAnimator("translationX", -dX)
        val animatorTranslationY = createPropertyAnimator("translationY", -dY)

        val animatorScaleX = createPropertyAnimator("scaleX", scaleTarget)
        val animatorScaleY = createPropertyAnimator("scaleY", scaleTarget)

//        val animatorPivotX = createPropertyAnimator("pivotX", pX)
//        val animatorPivotY = createPropertyAnimator("pivotY", pY)

        AnimatorSet().apply {
//            play(animatorTranslationX).with(animatorTranslationY)
            play(animatorScaleX).with(animatorScaleY)
//            play(animatorPivotX).with(animatorPivotY)
            addListener(animatorListener)
            start()
        }
    }

    private fun resetAnimation() {
        val animatorScaleX = createPropertyAnimator("scaleX", NORM_SCALE)
        val animatorScaleY = createPropertyAnimator("scaleY", NORM_SCALE)
//        val animatorPivotX = createPropertyAnimator("pivotX", (contentContainer.width / 2).toFloat())
//        val animatorPivotY = createPropertyAnimator("pivotY", (contentContainer.height / 2).toFloat())
        val animatorX = createPropertyAnimator("x", 0f)
        val animatorY = createPropertyAnimator("y", 0f)
        AnimatorSet().apply {
            play(animatorScaleX).with(animatorScaleY)
//            play(animatorPivotX).with(animatorPivotY)
            play(animatorX).with(animatorY)
            addListener(animatorListener)
            start()
        }
    }

    private fun createPropertyAnimator(propertyName: String, propertyTarget: Float) =
        ObjectAnimator.ofFloat(contentContainer, propertyName, propertyTarget).apply {
            duration = SCALE_DURATION
        }

    private fun createPointPivot() = findViewById<PointView>(R.id.point_pivot)

    private fun createSegmentView(segment: MetroLineSegmentUiModel, lineColor: Int): View {
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

    private fun View.setPivot(pX: Float, pY: Float) {
        this.pivotX = pX
        this.pivotY = pY
    }

    private fun View.hasScaleNearToMin() = this.scaleX < MIN_SCALE + (MAX_SCALE - MIN_SCALE) / 2

    private fun showBefore() {
        view01.text = "before:" +
                "pX = ${pointPivot.x}\n" +
                "pY=${pointPivot.y}\n"
    }

    private fun showAfter() {
        view03.text = "before:" +
                "pX = ${pointPivot.x}\n" +
                "pY=${pointPivot.y}\n"
    }
}