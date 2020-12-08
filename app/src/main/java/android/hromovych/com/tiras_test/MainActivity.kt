package android.hromovych.com.tiras_test

import android.hromovych.com.tiras_test.imageSources.FileNavigateDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FileNavigateDialog {
            Toast.makeText(this, it.size.toString(), Toast.LENGTH_SHORT).show()
        }.show(supportFragmentManager, null)
    }
}