package com.example.android.govguide

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.android.govguide.data_objects.*
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.representative_list_item.*

/**
 * Created by Geoff on 9/23/2017.
 */
class RepAdapter(val reps: Representatives, val activity: AppCompatActivity) :
        RecyclerView.Adapter<RepAdapter.RepViewHolder>() {

    val officials: MutableList<Pair<Office, Official>> = mutableListOf()
    var selectedOfficial: Official?
    var onClickFun: (Official?) -> Unit


    init {
        //filter out pres/VP
        reps.offices.filter { office -> !office.divisionId.equals("ocd-division/country:us") }
                .forEach {
                    for (i in it.officialIndices) {
                        officials.add(Pair(it, reps.officials[i]))
                    }
                }
        selectedOfficial = null
        onClickFun = {o -> selectedOfficial = o}
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepViewHolder {
        return RepViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.representative_list_item, parent, false), activity)
    }

    override fun onBindViewHolder(holder: RepViewHolder, position: Int) {
        val (office, official) = officials[position]
        holder.bind(official, office, onClickFun)
    }

    override fun getItemCount(): Int {
        return officials.size
    }



    class RepViewHolder(override val containerView: View, val activity: AppCompatActivity) :
            RecyclerView.ViewHolder(containerView), LayoutContainer,
            View.OnCreateContextMenuListener, View.OnClickListener, View.OnLongClickListener {
        init {
            containerView.setOnCreateContextMenuListener(this)
            containerView.setOnClickListener(this)
            containerView.setOnLongClickListener(this)
        }

        var official: Official? = null
        var officeTitle: String = ""
        var onClickFun: (Official?) -> Unit = { o -> Unit }

        override fun onClick(p0: View?) {
            activity.openContextMenu(containerView)
        }

        override fun onLongClick(p0: View?): Boolean {
            activity.closeContextMenu()
            return true
        }

        override fun onCreateContextMenu(p0: ContextMenu?, p1: View?, p2: ContextMenu.ContextMenuInfo?) {
            MenuInflater(containerView.context).inflate(R.menu.contact, p0)
            onClickFun(this.official)
        }

        fun bind(official: Official, office: Office, onClickFun: (Official?) -> Unit) {
            this.official = official
            officeTitle = office.name.replace("United States", "US")
            this.onClickFun = onClickFun
            tv_name.text = "${official.name} ${official.party.getPartyAbbrev()}"
            tv_title.text = officeTitle
            Picasso
                    .with(containerView.context)
                    .load(official.photoUrl)
                    .resize(150, 150)
                    .onlyScaleDown()
                    .centerCrop()
                    .into(iv_rep_photo)
        }
    }
}