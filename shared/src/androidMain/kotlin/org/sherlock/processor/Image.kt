package org.sherlock.processor

import android.net.Uri

actual class Image(
    val uri: Uri,
) {
    actual val key: String = uri.toString()
}