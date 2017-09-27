package com.example.android.govguide

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.govguide.data_objects.Address
import com.example.android.govguide.data_objects.Office
import com.example.android.govguide.data_objects.Official
import com.example.android.govguide.data_objects.Representatives
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.representative_list_item.*
//TODO open contact menu on long press
//https://developer.android.com/guide/topics/ui/menus.html#FloatingContextMenu
/**
 * Created by Geoff on 9/23/2017.
 */
class RepAdapter(val reps: Representatives, val onClickFun: (String, Official?) -> Unit) : RecyclerView.Adapter<RepAdapter.RepViewHolder>() {
    val officials: MutableList<Pair<Office, Official>> = arrayListOf()
    init {
        //filter out pres/VP
        reps.offices.filter { office -> !office.divisionId.equals("ocd-division/country:us")}
                        .forEach {
                            for (i in it.officialIndices) {
                                officials.add(Pair(it, reps.officials[i]))
                            }
                        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepViewHolder {
        return RepViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.representative_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RepViewHolder, position: Int) {
        val (office, official) = officials[position]
        holder.bind(official, office, onClickFun)
    }
    override fun getItemCount(): Int {
        return officials.size
    }

    class RepViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer, View.OnClickListener {
        init {
            containerView.setOnClickListener(this)
        }
        var official: Official? = null
        var officeTitle: String = ""
        var onClickFun: (String, Official?) -> Unit = {s, o -> Unit}

        fun bind(official: Official, office: Office, onClickFun: (String, Official?) -> Unit) {
            this.official = official
            officeTitle = office.name.replace("United States", "US")
            this.onClickFun = onClickFun
            tv_name.text = official.name
            tv_title.text = officeTitle
            Picasso
                    .with(containerView.context)
                    .load(official.photoUrl)
                    .resize(150, 150)
                    .onlyScaleDown()
                    .centerCrop()
                    .into(iv_rep_photo)
        }

        override fun onClick(p0: View?) {
            onClickFun(officeTitle, official)
        }

        fun addressAsString(address: Address): String {
            return "${if (address.line1.equals("")) "" else address.line1 + "\n" }" +
                    "${if (address.line2.equals("")) "" else address.line2 + "\n" }" +
                    "${if (address.line3.equals("")) "" else address.line3 + "\n" }" +
                    "${if (address.city.equals("")) "" else address.city + ", " }" +
                    "${if (address.state.equals("")) "" else address.state + " " }" +
                    "${address.zip}"
        }
    }
}