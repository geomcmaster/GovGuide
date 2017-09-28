package com.example.android.govguide

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.example.android.govguide.data_objects.Office
import com.example.android.govguide.data_objects.Official
import com.example.android.govguide.data_objects.Representatives
import com.example.android.govguide.utils.Api
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL

class MainActivity : AppCompatActivity() {

    var reps: Representatives? = null
    val api = Api()
    val sharedPrefChangeListener = { sharedPreferences: SharedPreferences, s: String ->
        getResult()
    }
    var repAdapter = RepAdapter(Representatives(arrayOf(), arrayOf()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        left_drawer.setAdapter(ArrayAdapter<String>(this, R.layout.drawer_list_item, resources.getStringArray(R.array.drawer_options)))
        left_drawer.setOnItemClickListener { adapterView, view, i, l ->
            if (resources.getStringArray(R.array.drawer_options)[i].equals(getString(R.string.settings))) {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(sharedPrefChangeListener)

        drawer_layout
                .addDrawerListener(
                        ActionBarDrawerToggle(
                                this, drawer_layout, R.string.app_name, R.string.app_name))

        rv_reps.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val r = reps
        if (r == null) {
            getResult()
        } else {
            repAdapter = RepAdapter(r)
            rv_reps.adapter = repAdapter
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        if (repAdapter.selectedOfficial != null) {
            when (item?.itemId) {
                null -> return super.onContextItemSelected(item)
                R.id.action_call -> {
                    val phones = repAdapter.selectedOfficial?.phones
                    if (phones == null || phones.size < 1) {
                        toast(getString(R.string.call_error))
                        return super.onContextItemSelected(item)
                    } else {
                        //TODO ask user to select a number if there's more than one
                        intent = Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:${phones[0]}"))
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }
                    }
                }
                R.id.action_email -> {
                    val emails = repAdapter.selectedOfficial?.emails
                    if (emails == null || emails.size < 1) {
                        toast(getString(R.string.email_error))
                        return super.onContextItemSelected(item)
                    } else {
                        intent = Intent(Intent.ACTION_SENDTO)
                        intent.setData(Uri.parse("mailto:"))
                        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emails[0]))
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }
                    }
                }
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(sharedPrefChangeListener)
    }

    fun showError() {
        pb_loading_reps.visibility = View.INVISIBLE
        tv_err_msg.visibility = View.VISIBLE
        rv_reps.visibility = View.INVISIBLE
    }

    fun showLoading() {
        tv_err_msg.visibility = View.INVISIBLE
        rv_reps.visibility = View.INVISIBLE
        pb_loading_reps.visibility = View.VISIBLE
    }

    fun showRecyclerView() {
        pb_loading_reps.visibility = View.INVISIBLE
        tv_err_msg.visibility = View.INVISIBLE
        rv_reps.visibility = View.VISIBLE
    }

    fun getResult() {
        doAsync {
            uiThread {
                showLoading()
            }
            val userLoc = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                    .getString(getString(R.string.pref_location_key), "")
            if (userLoc.isEmpty()) {
                uiThread {
                    showError()
                }
            } else {
                val urlString = Uri.parse(getString(R.string.google_civic_info_base_url))
                        .buildUpon()
                        .appendQueryParameter(
                                getString(R.string.address_param), userLoc)
                        .appendQueryParameter(getString(R.string.key_param), api.getCivicInfoKey())
                        .build()
                        .toString()
                val url = URL(urlString)
                var repsJson = ""
                try {
                    repsJson = url.readText()
                } catch (e: Exception) {
                    Log.e("CivicInfoApi", "Error reading from url", e)
                }
                if (repsJson.isEmpty()) {
                    uiThread {
                        showError()
                    }
                } else {
                    reps = Gson().fromJson(repsJson, Representatives::class.java)
                    uiThread {
                        val r = reps
                        if (r != null) {
                            showRecyclerView()
                            repAdapter = RepAdapter(r)
                            rv_reps.adapter = repAdapter
                        } else {
                            showError()
                        }
                    }
                }
            }
        }
    }
}
