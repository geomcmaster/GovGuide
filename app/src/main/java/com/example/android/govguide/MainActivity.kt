package com.example.android.govguide

import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.example.android.govguide.utils.Api
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL
import com.example.android.govguide.data_objects.*
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    var reps: Representatives? = null
    val api = Api()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_reps.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val r = reps
        if (r == null) {
            getResult()
        } else {
            rv_reps.adapter = RepAdapter(r)
        }
    }

    fun getResult() {
        doAsync {
            tv_err_msg.visibility = View.INVISIBLE
            pb_loading_reps.visibility = View.VISIBLE

            val url = URL(getString(R.string.default_test_url) +
                    api.getCivicInfoKey())
            val repsJson = url.readText()

            reps = Gson().fromJson(repsJson, Representatives::class.java)

            uiThread {
                pb_loading_reps.visibility = View.INVISIBLE
                val r = reps
                
                if (r != null) {
                    tv_err_msg.visibility = View.INVISIBLE
                    rv_reps.adapter = RepAdapter(r)
                    rv_reps.visibility = View.VISIBLE
                } else {
                    tv_err_msg.visibility = View.VISIBLE
                    rv_reps.visibility = View.INVISIBLE
                }
            }
        }
    }
}
