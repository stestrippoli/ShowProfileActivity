package com.example.showprofileactivity

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.showprofileactivity.placeholder.TimeSlot
import com.example.showprofileactivity.placeholder.TimeSlotCollection


class TimeSlotListFragment(
    private var values: MutableList<TimeSlot>

) : RecyclerView.Adapter<TimeSlotListFragment.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val v = LayoutInflater.from(parent.context)
            .inflate(
               R.layout.fragment_item,
                parent,
                false
            )



        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.card_title.text = item.title
        holder.card_description.text = item.description
        holder.card_date.text = item.date
        
        holder.itemView.setOnClickListener{v:View ->
            val b = bundleOf("item" to item.itemToJSON(position).toString())
            v.findNavController().navigate(R.id.nav_timeSlotDetailsFragment, b)
        }

        val btn = holder.itemView.findViewById<Button>(R.id.edit_btn)
        btn.setOnClickListener{
            val b = bundleOf("item" to item.itemToJSON(position).toString())
            it.findNavController().navigate(R.id.action_toEditFragment, b )
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card_title: TextView
        val card_description: TextView
        val card_date: TextView
    init {
        card_title = itemView.findViewById(R.id.card_title)
        card_description =itemView.findViewById(R.id.card_description)
        card_date = itemView.findViewById(R.id.card_date)
    }

    }

}