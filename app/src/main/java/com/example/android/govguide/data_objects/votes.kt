package com.example.android.govguide.data_objects

/**
 * Created by Geoff on 11/5/2017.
 */
data class Votes(val results: Results)

data class Results(val num_results: Int, val votes: Array<Vote>)

data class Vote(val chamber: String, val bill: Bill, val question: String = "",
                val description: String = "", val result: String, val total: Count)

data class Bill(val number: String = "", val title: String = "")

data class Count(val yes: Int, val no: Int, val present: Int, val not_voting: Int)