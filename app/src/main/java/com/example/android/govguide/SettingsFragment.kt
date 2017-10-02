package com.example.android.govguide


import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.jetbrains.anko.doAsync

//TODO error message on location retrieval error
/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragmentCompat() {
    var locationClient: FusedLocationProviderClient? = null
    lateinit var geoCoder: Geocoder
    val PERMISSIONS_REQUEST_LOCATION = 1

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> {
                if (locationClient == null) {
                    locationClient = LocationServices
                            .getFusedLocationProviderClient(this.context)
                }
                val permission = ContextCompat.checkSelfPermission(this.context,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    locationClient?.lastLocation?.addOnSuccessListener { loc ->
                        doAsync {
                            setPrefFromLocation(loc)
                        }
                    }
                }
            }
        }
    }

    fun setPrefFromLocation(loc: Location) {
        //TODO get all results and let user select?
        val addressList = geoCoder
                .getFromLocation(loc.latitude, loc.longitude, 1)
        if (addressList.size > 0) {
            val address = "${addressList[0].subThoroughfare} ${addressList[0].thoroughfare}, " +
                    "${addressList[0].locality}, " +
                    "${addressList[0].adminArea} " +
                    "${addressList[0].postalCode}"
            PreferenceManager.getDefaultSharedPreferences(this.context)
                    .edit().putString(getString(R.string.pref_location_key), address).apply()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref)

        geoCoder = Geocoder(this.context)

        findPreference(getString(R.string.pref_use_curr_loc_key)).onPreferenceClickListener =
                Preference.OnPreferenceClickListener { _ ->
                    if (locationClient == null) {
                        locationClient = LocationServices
                                .getFusedLocationProviderClient(this.context)
                    }
                    val permission = ContextCompat.checkSelfPermission(this.context,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    if (permission == PackageManager.PERMISSION_GRANTED) {
                        locationClient?.lastLocation?.addOnSuccessListener { loc ->
                            doAsync {
                                setPrefFromLocation(loc)
                            }
                        }
                    } else {
                        ActivityCompat.requestPermissions(this.activity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                PERMISSIONS_REQUEST_LOCATION)
                    }
                    true
        }
    }
}// Required empty public constructor
