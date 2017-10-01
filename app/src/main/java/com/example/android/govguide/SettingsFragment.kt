package com.example.android.govguide


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragmentCompat() {
    var locationClient: FusedLocationProviderClient? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref)
        findPreference(getString(R.string.pref_use_curr_loc_key)).onPreferenceClickListener =
                Preference.OnPreferenceClickListener { _ ->
                    if (locationClient == null) {
                        locationClient = LocationServices.getFusedLocationProviderClient(this.context)
                    }
                    //TODO request permission, get location and pass to callChangeListener
                    //https://developer.android.com/training/location/retrieve-current.html
                    //https://developer.android.com/training/location/display-address.html
                    //https://developer.android.com/training/permissions/requesting.html
                    findPreference(getString(R.string.pref_location_key)).callChangeListener("")
                    true
        }
    }
}// Required empty public constructor
