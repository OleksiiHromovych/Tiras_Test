package android.hromovych.com.tiras_test.imageSlider

import android.os.SystemClock
import android.view.View

abstract class TripleClickListener(val maxClickedTimeMillis: Long = 1500) : View.OnClickListener{

    private var currentClickCount = 0
    private var startClickedTimeMillis: Long = 0

    abstract fun onTripleClick(v: View?)

    override fun onClick(v: View?) {
        val currentTime = SystemClock.uptimeMillis()
        if ((currentTime - startClickedTimeMillis) < maxClickedTimeMillis) {
            currentClickCount++
            if (currentClickCount == 3)
                onTripleClick(v)
        } else {
            startClickedTimeMillis = currentTime
            currentClickCount = 1
        }
    }
}