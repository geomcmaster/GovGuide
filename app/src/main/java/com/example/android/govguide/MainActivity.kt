package com.example.android.govguide

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.android.govguide.utils.Api
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class MainActivity : AppCompatActivity() {

    var repsJson = ""
    val api = Api()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (repsJson.equals("")) {
            getResult()
        } else {
            test_text.text = repsJson
        }
    }

    fun getResult() {
        doAsync {
            val url = URL(getString(R.string.default_test_url) +
                    api.getCivicInfoKey())
            repsJson = url.readText()
            uiThread {
                test_text.text = repsJson
            }
        }
    }
}
