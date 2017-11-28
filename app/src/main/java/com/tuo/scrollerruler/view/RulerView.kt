package com.tuo.scrollerruler.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.OverScroller

/**
 * @describe TODO
 * 作者：Tuo on 2017/11/26 10:14
 * 邮箱：839539179@qq.com
 */
class RulerView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attributeSet, defStyleAttr) {


    // 实现惯性滑动
    private var mOverScroller: OverScroller = OverScroller(context)

    // 速度追踪器
    private var mVelocityTracker: VelocityTracker?
    // 最大速度
    private var mMaxVelocity = 0
    // 最小速度
    private var mMinVelocity = 0

    private var mPaint: Paint


    private var mLastX = 0f

    private val mMinValue = 0f

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

    private var mMidValue: Float = 0f


    init {
        mVelocityTracker = VelocityTracker.obtain()
        mMaxVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        mMinVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity

        mPaint = Paint()

        mCount = ((mMaxValue - mMinValue) / mSpaceNumber).toInt()

        mContentLength = mCount * mSpaceWidth


    }

    // 设置 选中刻度
    fun setCurrentValue(value: Float) {

        var v = value
        // 对设置的刻度进行大小精确
        if (v < mMinValue) {
            v = mMinValue
        }
        if (v > mMaxValue) {
            v = mMaxValue
        }


        val sx = ((v - 1.2f) / mSpaceNumber) * mSpaceWidth

        scrollTo(sx.toInt(), 0)

        mMidValue = v

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // 确定左右滑动边界
        mLeftBorder = -w / 2.toFloat()

        mRightBorder = mContentLength - w / 2

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        // 画顶部的水平线
        mPaint.color = Color.GRAY
        canvas.drawLine(0f, 0f, mContentLength, 0f, mPaint)

        // 画刻度 和 文字
        mPaint.color = Color.GRAY
        mPaint.strokeWidth = 3f
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.textSize = 48f

        Log.i("count", "$mCount")

        for (i in 0..mCount) {
            val startX = i * mSpaceWidth

            // 屏幕之外的内容不画 在 0到width 之间 加上 滑动的距离
            if (0 + scrollX < startX && startX < width + scrollX) {
                // 判读 是大刻度 还是小刻度
                val scaleHeight = if (i % mScaleStep == 0) mMaxScaleHeight else mMinScaleHeight
                canvas.drawLine(startX, 0f, startX, scaleHeight, mPaint)
                val text = mMinValue + i * mSpaceNumber
                if (i % mScaleStep == 0) {
                    canvas.drawText("$text", startX, mMidCursorHeight + 100f, mPaint)
                    Log.i("i", "$i")
                }
            }


        }


        // 画光标 为了保持在滑动过程中 光标不动 光标的位置 需要通过 已滑动的距离动态计算
        mPaint.color = Color.GREEN
        mPaint.strokeWidth = 6f
        canvas.drawLine(width / 2.toFloat() + scrollX, 0f, width / 2.toFloat() + scrollX, mMidCursorHeight, mPaint)

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val currentX = event.x
        val action = event.action

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mOverScroller.isFinished) {
                    mOverScroller.abortAnimation()
                }

                mLastX = currentX

            }

            MotionEvent.ACTION_MOVE -> {

                var scrollerX = mLastX - currentX

                var x = scrollX + scrollerX.toInt()

                // 滑动到左右边界不再滑动
                when {
                    x < mLeftBorder -> return true
                    x > mRightBorder -> return true
                    else -> scrollTo(x, 0)
                }

                mLastX = currentX
            }

            MotionEvent.ACTION_UP -> {
                mVelocityTracker!!.computeCurrentVelocity(1000, mMaxVelocity.toFloat())
                val velocityX = mVelocityTracker!!.xVelocity

                if (Math.abs(velocityX) > mMinVelocity) {
                    mOverScroller.fling(scrollX, 0, -velocityX.toInt(), 0, mLeftBorder.toInt(), mRightBorder.toInt(), 0, 0)
                    invalidate()
                } else {
                    val sx = Math.round(scrollX / mSpaceWidth) * mSpaceWidth
                    Log.i("sx_x", "${Math.round(scrollX / mSpaceWidth)}" + "   " + "$scrollX")
                    mOverScroller.startScroll(scrollX, 0, (sx - scrollX).toInt(), 0, 1000)
                    invalidate()
                }



                recycleVelocity()
            }

            MotionEvent.ACTION_CANCEL -> {
                if (!mOverScroller.isFinished) {
                    mOverScroller.abortAnimation()
                }
                recycleVelocity()
            }
        }

        return true
    }


    override fun computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(mOverScroller.currX, mOverScroller.currY)
            postInvalidate()
        }
    }


    //回收速度追踪器
    private fun recycleVelocity() {
        if (null != mVelocityTracker) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

}