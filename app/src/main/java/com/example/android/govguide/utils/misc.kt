package com.example.android.govguide.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Created by Geoff on 9/28/2017.
 */
/**
 * Starts an activity if possible. Returns true if successful.
 */
fun Intent.safeStartActivity(srcActivity: Activity): Boolean {
    if (this.resolveActivity(srcActivity.packageManager) != null) {
        srcActivity.startActivity(this)
        return true
    } else {
        return false
    }
}