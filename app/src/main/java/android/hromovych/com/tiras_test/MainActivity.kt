package android.hromovych.com.tiras_test

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hromovych.com.tiras_test.extentions.findImages
import android.hromovych.com.tiras_test.imageSlider.PagerLab
import android.hromovych.com.tiras_test.imageSlider.TripleClickListener
import android.hromovych.com.tiras_test.imageSources.DownloadImage
import android.hromovych.com.tiras_test.imageSources.FileNavigateDialog
import android.hromovych.com.tiras_test.receivers.PowerConnectionService
import android.hromovych.com.tiras_test.receivers.StartSlideShowReceiver
import android.hromovych.com.tiras_test.settings.MainSettingsActivity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    private lateinit var pagerLab: PagerLab

    private var pagerLabTablet: PagerLab? = null

    private lateinit var images: List<Bitmap>

    private var isActivityLocked = true

    companion object {
        const val SHARED_PREFERENCES_NAME = "android.hromovych.com.tiras_test"
        const val PREFERENCES_PATH = "path"
        const val PREFERENCES_WITH_NESTED = "with nested"
        const val OPEN_DIRECTORY_REQUEST_CODE = 404

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            startService(Intent(this, PowerConnectionService::class.java))

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        initViews()

        lockActivity(isActivityLocked)

        pagerLab.periodMs = getPeriod()

        startShow()
    }

    private fun initViews() {
        pagerLab = PagerLab(this, findViewById(R.id.pager))

        pagerLab.onPagerClickListener = onPagerClickListener

        findViewById<ViewPager>(R.id.pagerTablet)?.let {
            pagerLabTablet = PagerLab(this, it)
            pagerLabTablet!!.onPagerClickListener = onPagerClickListener
        }

        progressBar = findViewById(R.id.progressBar)
    }

    override fun onResume() {
        super.onResume()
        val newPeriodMs = getPeriod()
        if (newPeriodMs != pagerLab.periodMs) {
            pagerLab.periodMs = newPeriodMs
        }
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
            else -> Toast.makeText(this, "${item.title} not implement", Toast.LENGTH_SHORT)
                .show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE && resultCode == RESULT_OK){
            val photosFolderPath: String = data?.data!!.path!!.substringBeforeLast("/")
            Toast.makeText(this, "Images from folder at $photosFolderPath", Toast.LENGTH_SHORT).show()
            fileNavigateAction(photosFolderPath, false)

        }
    }

    private val onPagerClickListener = object : TripleClickListener() {
        override fun onTripleClick(v: View?) {
            isActivityLocked = !isActivityLocked
            lockActivity(isActivityLocked)
        }
    }

    private fun startShow() {
        val isImagesFromLocal = PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean(getString(R.string.USE_LOCAL_IMAGES), false)

        if (isImagesFromLocal) {
            val preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
            val path = preferences.getString(PREFERENCES_PATH, null)
            val withNested = preferences.getBoolean(PREFERENCES_WITH_NESTED, false)

            if (path == null)
                showFileNavigateDialog()    //pick new path
            else
                fileNavigateAction(path, withNested)
        } else
            setImageFromInternet()
    }

    private fun getPeriod(): Long {
        var interval = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt(getString(R.string.LENGTH_INTERVAL), 5)  //need in ms

        if (interval == 0)
            interval = 1

        return interval * 1000L
    }

    private fun showFileNavigateDialog() {
        /* Show dialog for images picker from local storage */
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
        } else {
            FileNavigateDialog { path, withNested ->
                fileNavigateAction(path, withNested)
            }.show(supportFragmentManager, null)
        }
    }

    private fun initPagers(images: List<Bitmap>) {

        if (pagerLabTablet != null) {
            val halfIndex = images.size / 2
            val firstHalf = images.subList(0, halfIndex)
            val secondHalf = images.subList(halfIndex, images.size)
            pagerLab.initPager(firstHalf)
            pagerLabTablet!!.initPager(secondHalf)

        } else
            pagerLab.initPager(images)
    }

    private fun fileNavigateAction(path: String, withNested: Boolean) {
        // anonymous function fro path pick dialog

        val sharedPref = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(PREFERENCES_PATH, path)
            putBoolean(PREFERENCES_WITH_NESTED, withNested)
            apply()
        }
        try {
            images = File(path).findImages(withNested)

        } catch (e: Exception) {
            Log.e("TAG", "fileNavigateAction: ${e.message}", )
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "${images.size} images picked", Toast.LENGTH_SHORT).show()
        initPagers(images)

    }

    private fun lockActivity(lock: Boolean) {
        if (lock) {
            supportActionBar?.hide()
            showNavigationBar(false)
        } else {
            supportActionBar?.show()
            showNavigationBar(true)
        }
    }

    private fun showNavigationBar(isShow: Boolean) {
        val flags = if (isShow) {
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
        window.decorView.apply {
            systemUiVisibility = flags
            setOnSystemUiVisibilityChangeListener {
                if (it and View.SYSTEM_UI_FLAG_FULLSCREEN == 0)
                    this.systemUiVisibility = flags
            }
        }
    }

    private fun setImageFromInternet() {
        DownloadImage(progressBar) {
            images = it
            initPagers(images)
        }.execute(*resources.getStringArray(R.array.image_urls))
    }
}
