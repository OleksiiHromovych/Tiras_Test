package android.hromovych.com.tiras_test.extentions

import java.io.File

fun File.isImage() =
    this.name.toLowerCase().substringAfterLast(".") in
            arrayOf("jpg", "png", "gif", "jpeg")

fun File.findImages(withNested: Boolean): List<String> {
    val images = mutableListOf<String>()

    this.listFiles()?.forEach {
        if (withNested && it.isDirectory && !it.isHidden)
            images.addAll(findImages(withNested))
        else
            if (it.isImage())
                images.add(it.absolutePath)
    }

    return images
}
