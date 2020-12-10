package android.hromovych.com.tiras_test.imageSlider

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager
import java.util.*
import kotlin.properties.Delegates

class PagerLab(val context: Context, val pager: ViewPager) {

    private var timer: Timer? = null
    private val DELAY_MS: Long = 1000

    var periodMs: Long = 5000
        set(value) {
            field = value
            if (update != null)
                updateTimer()
        }

    var onPagerClickListener: TripleClickListener? = null

    var currentPage = 0
        private set

    private var update: Runnable? = null

    @SuppressLint("ClickableViewAccessibility")
    fun initPager(images: List<Bitmap>) {

        pager.addOnPageChangeListener(onPageChangeListener)

        pager.setOnTouchListener(onTouchListener)

        pager.setPageTransformer(false, FadePageTransformer())

        updatePagerContent(images)
    }


    fun updatePagerContent(images: List<Bitmap>) {
        pager.adapter = PageView(context, images)

        update = Runnable {
            if (currentPage == images.size)
                currentPage = 0
            pager.setCurrentItem(currentPage++, true)
        }
        updateTimer()
    }

    private fun updateTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
        }

        val handler = Handler()

        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update!!)
            }
        }, DELAY_MS, periodMs)

    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            currentPage = position
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    private val onTouchListener = object : View.OnTouchListener {
        private var moved by Delegates.notNull<Boolean>()

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    moved = false
                }
                MotionEvent.ACTION_MOVE -> {
                    moved = true
                }
                MotionEvent.ACTION_UP -> {
                    if (!moved) {
                        onPagerClickListener?.onClick(v)
                        return true
                    }
                }
            }
            return true
        }
    }
}