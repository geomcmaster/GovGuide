package com.example.android.govguide

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.govguide.data_objects.Results
import com.example.android.govguide.data_objects.Vote
import kotlinx.android.synthetic.main.legislation_list_item.*

/**
 * Created by Geoff on 11/5/2017.
 */
class VoteAdapter(val results: Results, val activity: AppCompatActivity) : RecyclerView.Adapter<VoteAdapter.VoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        return VoteViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.legislation_list_item, parent, false), activity)
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        holder.bind(results.votes[position])
    }

    override fun getItemCount(): Int {
        return results.num_results
    }

    class VoteViewHolder(val containerView: View, val activity: AppCompatActivity) :
            RecyclerView.ViewHolder(containerView) {
        fun bind(vote: Vote) {
            activity.tv_bill_number.text = vote.bill.number
            activity.tv_bill_name.text = vote.bill.title
            activity.tv_chamber.text = vote.chamber
            activity.tv_result.text = vote.result
            activity.tv_yea_count.text = vote.total.yes.toString()
            activity.tv_nay_count.text = vote.total.no.toString()
            activity.tv_present_count.text = vote.total.present.toString()
            activity.tv_not_voting_count.text = vote.total.not_voting.toString()
        }
    }
}