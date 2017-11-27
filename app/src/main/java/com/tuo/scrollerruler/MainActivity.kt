package com.tuo.scrollerruler

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.tuo.scrollerruler.view.RulerView


class MainActivity : AppCompatActivity() {

    private lateinit var rv: RulerView

    private lateinit var tv: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv = findViewById(R.id.rv)
        tv = findViewById(R.id.tv)

        rv.setCurrentValue(4.7f)
        rv.setCurrentValue(0.0f)


    }
}
