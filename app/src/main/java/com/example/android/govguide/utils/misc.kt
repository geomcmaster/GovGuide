package com.example.android.govguide.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.support.v7.preference.PreferenceManager
import com.example.android.govguide.R

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

fun setPrefFromLocation(context: Context, geoCoder: Geocoder, loc: Location) {
    //TODO get all results and let user select?
    val addressList = geoCoder
            .getFromLocation(loc.latitude, loc.longitude, 1)
    if (addressList.size > 0) {
        val address = "${addressList[0].subThoroughfare} ${addressList[0].thoroughfare}, " +
                "${addressList[0].locality}, " +
                "${addressList[0].adminArea} " +
                "${addressList[0].postalCode}"
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(context.getString(R.string.pref_location_key), address).apply()
    }
}