package com.tuo.scrollerruler.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.OverScroller

/**
 * @describe TODO
 * 作者：Tuo on 2017/11/27 10:50
 * 邮箱：839539179@qq.com
 */
class ScrollerRuler
@JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(context, attributeSet, defStyleAttr) {


    // 实现惯性滑动
    private var mOverScroller: OverScroller = OverScroller(context)

    // 速度追踪器
    private var mVelocityTracker: VelocityTracker? = null
    // 最大速度
    private var mMaxVelocity = 0
    // 最小速度
    private var mMinVelocity = 0

    private var mPaint: Paint


    private var mLastX = 0f
    private val mMinValue = 0f
    private var mMidValue = 5f
    private val mMaxValue = 10f

    //两个小刻度之间的距离 单位 px
    private val mSpaceWidth = 45f

    //两个小刻度之间的数字间隔 比如 0.1~0.2   0.1~0.3   1~2
    private val mSpaceNumber = 0.1f

    private var mCount: Int = 0

    //尺子的长度
    private var mContentLength = 0f

    //小刻度的高度 单位 px
    private val mMinScaleHeight = 30f

    //大刻度的高度 单位 px
    private val mMaxScaleHeight = 60f

    //两个大刻度之间有几个小刻度
    private val mScaleStep = 10

    //屏幕中心的光标高度
    private val mMidCursorHeight = 100f

    // 左边滑动边距
    private var mLeftBorder: Float = 0f
    // 右边滑动边距
    private var mRightBorder: Float = 0f


    init {
        mVelocityTracker = VelocityTracker.obtain()
        mMaxVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        mMinVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity

        mPaint = Paint()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


    }
}