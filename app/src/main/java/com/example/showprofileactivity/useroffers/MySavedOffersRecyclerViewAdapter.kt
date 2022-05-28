package com.example.showprofileactivity.useroffers

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.showprofileactivity.databinding.FragmentSavedOffersBinding
import com.example.showprofileactivity.offers.placeholder.Offer

class MySavedOffersRecyclerViewAdapter(
    private val values: List<Offer>, private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<MySavedOffersRecyclerViewAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentSavedOffersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text = item.title
        holder.description.text = item.description
        val creatorText = item.creator + " (" + item.location + ")"
        holder.creator.text = creatorText
        val hoursText = item.hours.toString() + "\nhours"
        holder.hours.text = hoursText

        holder.card.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentSavedOffersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.cardTitle
        val description: TextView = binding.cardDescription
        val creator: TextView = binding.cardCreator
        val hours: TextView = binding.cardHours
        val card: CardView = binding.cardViewOuter

        override fun toString(): String {
            return super.toString() + " '" + title.text + " [" + creator.text + "]'"
        }
    }

}