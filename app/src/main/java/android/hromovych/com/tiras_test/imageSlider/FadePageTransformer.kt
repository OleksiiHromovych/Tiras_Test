package android.hromovych.com.tiras_test.imageSlider

import android.view.View
import androidx.viewpager.widget.ViewPager

class FadePageTransformer : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {

        page.alpha = 0.4f
        page.visibility = View.VISIBLE

        // Start Animation for a short period of time
        page.animate()
            .alpha(1f).duration = 1000
//                page.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    }
}