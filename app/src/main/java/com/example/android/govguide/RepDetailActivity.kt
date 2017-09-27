package com.example.android.govguide

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_rep_detail.*
//TODO enable back arrow
class RepDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rep_detail)
        val recievedIntent = intent
        val bundle = recievedIntent.extras
        tv_name.text = bundle.getString(getString(R.string.key_rep_name))
        tv_office.text = bundle.getString(getString(R.string.key_office))
        tv_party.text = bundle.getString(getString(R.string.key_party))
        //TODO handle multiple items and empty items
        //TODO add more info to this page
        val phones = bundle.getStringArray(getString(R.string.key_phone))
        val emails = bundle.getStringArray(getString(R.string.key_email))
        val websites = bundle.getStringArray(getString(R.string.key_website))
        tv_phone.text = if (phones.size > 0) phones[0] else ""
        tv_email.text =
                if (emails.size > 0) emails[0]
                else "e-mail not found"
        tv_website.text =
                if (websites.size > 0) Html.fromHtml("<a href=${websites[0]}> website ")
                else "website not found"
    }
}
