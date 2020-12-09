package android.hromovych.com.tiras_test

import android.content.Context
import android.content.Intent
import android.hromovych.com.tiras_test.extentions.findImages
import android.hromovych.com.tiras_test.imageSlider.PageView
import android.hromovych.com.tiras_test.imageSources.FileNavigateDialog
import android.hromovych.com.tiras_test.receivers.StartSlideShowReceiver
import android.hromovych.com.tiras_test.settings.MainSettingsActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mPager: ViewPager
    private lateinit var adapter: PageView

    private lateinit var images: List<String>

    private var timer: Timer? = null
    private val DELAY_MS: Long = 1000
    private var periodMs: Long = 5000


    var currentPage = 0

    companion object {
        const val SHARED_PREFERENCES_NAME = "android.hromovych.com.tiras_test"
        const val PREFERENCES_PATH = "path"
        const val PREFERENCES_WITH_NESTED = "with nested"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPager = findViewById(R.id.pager)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
//        supportActionBar?.hide()
        supportActionBar?.show()

        val preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val path = preferences.getString(PREFERENCES_PATH, null)
        val withNested = preferences.getBoolean(PREFERENCES_WITH_NESTED, false)

        periodMs = getPeriod()

        if (path == null)
            showFileNavigateDialog()
        else
            fileNavigateAction(path, withNested)
    }

    override fun onResume() {
        super.onResume()
        val newPeriodMs = getPeriod()
        if (newPeriodMs != periodMs) {
            Log.d("TAG", "onResume: $periodMs")
            periodMs = newPeriodMs
            updatePager()
        }
    }

    private fun getPeriod(): Long {
        var interval = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt(getString(R.string.LENGTH_INTERVAL), 5)  //need in ms

        if (interval == 0)
            interval = 1

        return interval * 1000L
    }

    private fun showFileNavigateDialog() {
        FileNavigateDialog { path, withNested ->

            val sharedPref = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(PREFERENCES_PATH, path)
                putBoolean(PREFERENCES_WITH_NESTED, withNested)
                apply()
            }
            fileNavigateAction(path, withNested)
        }.show(supportFragmentManager, null)

    }

    private fun fileNavigateAction(path: String, withNested: Boolean) {
        images = File(path).findImages(withNested)
        adapter = PageView(this, images)
        mPager.adapter = adapter
        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

        })

        updatePager()
    }

    private fun updatePager() {
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
        }
        val handler = Handler()
        val update = Runnable {
            if (currentPage == images.size)
                currentPage = 0
            mPager.setCurrentItem(currentPage++, true)
        }
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }

        }, DELAY_MS, periodMs)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_choice_images -> showFileNavigateDialog()
            R.id.action_start_end_time -> {
                StartSlideShowReceiver().setAlarm(this)
            }
            R.id.action_settings -> {
                startActivity(Intent(this, MainSettingsActivity::class.java))
            }
            else -> Toast.makeText(this, "${item.title} not implement", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}