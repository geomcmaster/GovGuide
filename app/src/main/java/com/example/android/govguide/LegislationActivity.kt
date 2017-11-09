package com.example.android.govguide

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class LegislationActivity : AppCompatActivity() {

    lateinit var drawerToggle: ActionBarDrawerToggle

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
}
