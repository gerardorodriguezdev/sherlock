package org.sherlock.processor

import android.net.Uri

class AndroidImage(val uri: Uri) : Image {
    override val key: String = uri.toString()
}