package android.hromovych.com.tiras_test.extentions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

fun File.isImage() =
    this.name.toLowerCase().substringAfterLast(".") in
            arrayOf("jpg", "png", "gif", "jpeg")

fun File.findImages(withNested: Boolean): List<Bitmap> {
    val images = mutableListOf<Bitmap>()

    this.listFiles()?.forEach {
        if (withNested && it.isDirectory && !it.isHidden)
            images.addAll(findImages(withNested))
        else
            if (it.isImage())
                images.add(BitmapFactory.decodeFile(it.absolutePath)!!)
    }

    return images
}
