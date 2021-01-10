package android.hromovych.com.tiras_test.imageSources

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ProgressBar
import kotlinx.coroutines.*
import java.net.URL
import kotlin.coroutines.CoroutineContext

class DownloadImageCoroutines(
    val mProgressBar: ProgressBar,
    val onPostExecuteAction: (List<Bitmap>) -> Unit
) : CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    fun execute(images: Array<String>) = launch {
        onPreExecute()
        val result = doInBackground(images)
        onPostExecute(result)
    }

    private fun onPreExecute() {
        mProgressBar.visibility = View.VISIBLE
    }

    private suspend fun doInBackground(images: Array<String>): List<Bitmap> =
        withContext(Dispatchers.IO) {
            val list = mutableListOf<Bitmap>()
            images.forEach {
                loadImageFromWebOperations(it)?.let { it1 -> list.add(it1) }
            }
            return@withContext list
        }


    private fun onPostExecute(result: List<Bitmap>) {
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

