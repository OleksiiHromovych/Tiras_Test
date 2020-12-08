package android.hromovych.com.tiras_test.extentions

import java.io.File

fun File.isImage() =
    this.name.toLowerCase().substringAfterLast(".") in
            arrayOf("jpg", "png", "gif", "jpeg")
