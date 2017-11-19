package com.example.android.govguide

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import com.example.android.govguide.data_objects.Representatives
import com.example.android.govguide.data_objects.Results
import com.example.android.govguide.utils.setPrefFromLocation
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_legislation.drawer_layout
import kotlinx.android.synthetic.main.activity_legislation.left_drawer
import kotlinx.android.synthetic.main.activity_legislation.rv_leg
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LegislationActivity : AppCompatActivity() {

    var leg: Results? = null

    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var voteAdapter: VoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legislation)

        //drawer//
        drawerToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.drawer_open,
                R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_48px)
        drawerToggle.isDrawerIndicatorEnabled = true

        left_drawer.setNavigationItemSelectedListener { item ->
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

        drawer_layout
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
                if (!drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.openDrawer(GravityCompat.START)
                } else {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Retrieves JSON data from API, parses it, and sets recycler view adapter
     */
    fun getResult() {
        //TODO implement
    }
}
