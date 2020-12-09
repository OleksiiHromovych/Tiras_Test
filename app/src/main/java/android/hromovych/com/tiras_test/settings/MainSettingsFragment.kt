package android.hromovych.com.tiras_test.settings

import android.hromovych.com.tiras_test.R
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class MainSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preferences, rootKey)
    }
}