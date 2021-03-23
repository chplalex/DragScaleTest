package com.chplalex.dragscaletest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var mainLayout: FrameLayout
    private lateinit var contentContainer: FrameLayout
    private lateinit var view01: TextView
    private lateinit var view02: TextView
    private lateinit var view03: TextView
    private lateinit var view04: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initLayouts()
        initViews()
    }

    private fun initLayouts() {
        mainLayout = findViewById(R.id.main_layout)
        contentContainer = layoutInflater.inflate(R.layout.content_container, mainLayout) as FrameLayout
        contentContainer.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, Gravity.CENTER)
    }

    private fun initViews() {
        view01 = createView(R.id.view01)
        view02 = createView(R.id.view02)
        view03 = createView(R.id.view03)
        view04 = createView(R.id.view04)
    }

    private fun createView(id: Int) = findViewById<MainTextView>(id)
}