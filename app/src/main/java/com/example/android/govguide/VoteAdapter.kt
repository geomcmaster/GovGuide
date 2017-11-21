package com.example.android.govguide

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.govguide.R.id.tv_bill_number
import com.example.android.govguide.data_objects.Results
import com.example.android.govguide.data_objects.Vote
import com.example.android.govguide.data_objects.Votes
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_legislation.*
import kotlinx.android.synthetic.main.legislation_list_item.*
import kotlinx.android.synthetic.main.legislation_list_item.view.*

/**
 * Created by Geoff on 11/5/2017.
 */
class VoteAdapter(val votes: Votes, val activity: AppCompatActivity) : RecyclerView.Adapter<VoteAdapter.VoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        return VoteViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.legislation_list_item, parent, false), activity)
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        holder.bind(votes.results.votes[position])
    }

    override fun getItemCount(): Int {
        return votes.results.num_results
    }

    class VoteViewHolder(override val containerView: View, val activity: AppCompatActivity) :
            RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(vote: Vote) {
            tv_bill_number.text = vote.bill.number
            if (vote.bill.title != null && !vote.bill.title.isEmpty()) {
                tv_bill_name.text = vote.bill.title
            } else {
                tv_bill_name.text = vote.description
            }
            tv_chamber.text = vote.chamber + " vote: "
            tv_result.text = vote.result
            tv_yea_count.text = vote.total.yes.toString()
            tv_nay_count.text = vote.total.no.toString()
            tv_present_count.text = vote.total.present.toString()
            tv_not_voting_count.text = vote.total.not_voting.toString()
        }
    }
}