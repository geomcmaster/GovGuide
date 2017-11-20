package com.example.android.govguide

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.example.android.govguide.data_objects.Results
import com.example.android.govguide.data_objects.Votes
import com.example.android.govguide.utils.Api
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_legislation.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class LegislationActivity : AppCompatActivity() {

    val api = Api()
    var leg: Votes? = null

    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var voteAdapter: VoteAdapter
    lateinit var activity: AppCompatActivity    //gets passed to ViewHolder so we can create context menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legislation)

        activity = this

        //drawer//
        drawerToggle = ActionBarDrawerToggle(this, drawer_layout_leg, R.string.drawer_open,
                R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_48px)
        drawerToggle.isDrawerIndicatorEnabled = true

        left_drawer_leg.setNavigationItemSelectedListener { item ->
            when (item.title) {
                getString(R.string.representatives) -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                getString(R.string.legislation) -> {
                    startActivity(Intent(this, LegislationActivity::class.java))
                    true
                }
                getString(R.string.settings) -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        drawer_layout_leg
                .addDrawerListener(drawerToggle)
        //////////

        //recycler view//
        rv_leg.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val l = leg
        if (l == null) {
            getResult()
        } else {
            voteAdapter = VoteAdapter(l, this)
            rv_leg.adapter = voteAdapter
        }
        /////////////////
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (!drawer_layout_leg.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout_leg.openDrawer(GravityCompat.START)
                } else {
                    drawer_layout_leg.closeDrawer(GravityCompat.START)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Retrieves JSON data from API, parses it, and sets recycler view adapter
     */
    fun getResult() {
        doAsync {
            uiThread {
                showLoading()
            }

            val client = OkHttpClient()
            val req = Request.Builder()
                    .header(getString(R.string.api_header_field), api.getCongressKey())
                    .url(getString(R.string.propublica_vote_base_url))
                    .build()
            val response = client.newCall(req).execute();
            val responseBody = response.body()
            if (!response.isSuccessful || responseBody == null) {
                uiThread {
                    showError()
                }
            } else {
                leg = Gson().fromJson(responseBody.charStream(), Votes::class.java)
                uiThread {
                    val l = leg
                    if (l != null) {
                        showRecyclerView()
                        voteAdapter = VoteAdapter(l, activity)
                        rv_leg.adapter = voteAdapter
                    } else {
                        showError()
                    }
                }
            }
        }
    }

    /*
    FUNCTIONS FOR MODIFYING VISIBILITY
    */
    fun showError() {
        pb_loading_leg.visibility = View.INVISIBLE
        tv_leg_err_msg.visibility = View.VISIBLE
        rv_leg.visibility = View.INVISIBLE
    }

    fun showLoading() {
        tv_leg_err_msg.visibility = View.INVISIBLE
        rv_leg.visibility = View.INVISIBLE
        pb_loading_leg.visibility = View.VISIBLE
    }

    fun showRecyclerView() {
        pb_loading_leg.visibility = View.INVISIBLE
        tv_leg_err_msg.visibility = View.INVISIBLE
        rv_leg.visibility = View.VISIBLE
    }
}
