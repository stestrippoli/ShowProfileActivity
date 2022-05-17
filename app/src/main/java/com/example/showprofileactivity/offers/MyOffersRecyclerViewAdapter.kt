package com.example.showprofileactivity.offers

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.showprofileactivity.databinding.FragmentOffersBinding
import com.example.showprofileactivity.offers.placeholder.Offer
import android.view.*
import androidx.cardview.widget.CardView
import com.example.showprofileactivity.offers.placeholder.OffersCollection
import java.util.*

class MyOffersRecyclerViewAdapter(private var values: List<Offer>,
    private val itemClickListener: ItemClickListener)
    : RecyclerView.Adapter<MyOffersRecyclerViewAdapter.ViewHolder>(){

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    return ViewHolder(FragmentOffersBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    fun setFilteredList(newList: List<Offer>) {
        values = newList
        notifyDataSetChanged()
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

    inner class ViewHolder(binding: FragmentOffersBinding) : RecyclerView.ViewHolder(binding.root) {
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