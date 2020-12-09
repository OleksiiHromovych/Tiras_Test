package android.hromovych.com.tiras_test.imageSources

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.View
import android.widget.ProgressBar
import java.net.URL

class DownloadImage(val mProgressBar: ProgressBar, val onPostExecuteAction: (List<Bitmap>) -> Unit) : AsyncTask<String, Void, List<Bitmap>>() {


    override fun onPreExecute() {
        super.onPreExecute()
        mProgressBar.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg params: String?): List<Bitmap> {
        val list = mutableListOf<Bitmap>()
        params.forEach {
            loadImageFromWebOperations(it)?.let { it1 -> list.add(it1) }
        }
        return list
    }

    override fun onPostExecute(result: List<Bitmap>) {
        mProgressBar.visibility = View.GONE
        onPostExecuteAction(result)
    }

    private fun loadImageFromWebOperations(url: String?): Bitmap? {
        return try {
            val inputStream = URL(url).openStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
}

