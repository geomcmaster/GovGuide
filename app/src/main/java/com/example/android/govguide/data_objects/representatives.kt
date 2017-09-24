package com.example.android.govguide.data_objects

/**
 * Data classes to be used by gson for holding representative data
 *
 * Created by Geoff on 9/23/2017.
 */
data class Representatives(val offices: Array<Office>, val officials: Array<Official>)

data class Office(val name: String, val divisionId: String, val levels: Array<String>,
                  val roles: Array<String>, val sources: Source, val officialIndices: IntArray)

data class Source(val name: String, val official: Boolean)

data class Official(val name: String, val address: Array<Address>, val party: String,
                    val phones: Array<String>, val urls: Array<String>, val photoUrl: String,
                    val emails: Array<String>, val channels: Array<Channel>)

data class Address(val line1: String = "", val line2: String = "", val line3:String = "", val city: String = "",
                   val state: String = "", val zip: String = "")

data class Channel(val type: String, val id: String)