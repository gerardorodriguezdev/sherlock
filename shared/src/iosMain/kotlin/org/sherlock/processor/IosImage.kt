package org.sherlock.processor

import platform.UIKit.UIImage

class IosImage(
    override val key: String,
    val uiImage: UIImage,
) : Image