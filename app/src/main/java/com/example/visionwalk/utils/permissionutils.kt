package com.example.visionwalk.utils

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat

object PermissionUtils {

    fun requestAll(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS
            ),
            100
        )
    }
}
