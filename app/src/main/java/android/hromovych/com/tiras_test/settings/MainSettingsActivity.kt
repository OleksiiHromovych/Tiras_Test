package android.hromovych.com.tiras_test.settings

import android.hromovych.com.tiras_test.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, MainSettingsFragment())
            .commit()
    }
}