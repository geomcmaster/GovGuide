package com.example.android.govguide


import android.Manifest
import android.content.Context
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
import com.example.android.govguide.utils.setPrefFromLocation
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
                        val context = this.context
                        doAsync {
                            setPrefFromLocation(context, geoCoder, loc)
                        }
                    }
                }
            }
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
                            if (loc != null) {
                                val context = this.context
                                doAsync {
                                    setPrefFromLocation(context, geoCoder, loc)
                                }
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
