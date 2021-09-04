package com.soten.memo.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.widget.Toast
import com.soten.memo.MemoApplication

object CameraUtil {
    fun rotate(bitmap: Bitmap?, degrees: Int): Bitmap? {
        var rotateBitmap = bitmap
        if (degrees != 0 && rotateBitmap != null) {
            val matrix = Matrix()
            matrix.setRotate(degrees.toFloat(), rotateBitmap.width.toFloat() / 2,
                rotateBitmap.height.toFloat() / 2)
            try {
                val converted = Bitmap.createBitmap(rotateBitmap, 0, 0,
                    rotateBitmap.width, rotateBitmap.height, matrix, true)
                if (rotateBitmap != converted) {
                    rotateBitmap.recycle()
                    rotateBitmap = converted
                    val options = BitmapFactory.Options()
                    options.inSampleSize = 4
                    rotateBitmap = Bitmap.createScaledBitmap(rotateBitmap, 1280, 1280, true)
                }
            } catch (ex: OutOfMemoryError) {
                Toast.makeText(MemoApplication.appContext, "사진 회전 실패...", Toast.LENGTH_SHORT).show()
            }
        }
        return rotateBitmap
    }

    fun exifOrientationToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }
}