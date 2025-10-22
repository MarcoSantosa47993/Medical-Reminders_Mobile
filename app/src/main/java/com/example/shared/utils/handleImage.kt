package com.example.shared.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


fun ByteArray.toImageBitmap(): Bitmap {
    val x = BitmapFactory.decodeByteArray(this, 0, this.size)
    return x
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}