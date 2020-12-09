package android.hromovych.com.tiras_test.imageSlider

import android.content.Context
import android.graphics.BitmapFactory
import android.hromovych.com.tiras_test.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter

class PageView(val context: Context, val images: List<String>) : PagerAdapter() {

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean =
        view == `object` as RelativeLayout

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rl = inflater.inflate(R.layout.swipe_fragment, container, false)
        val image = rl.findViewById<ImageView>(R.id.imageView)
        image.setImageBitmap(BitmapFactory.decodeFile(images[position]))
        container.addView(rl)
        return rl
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}