package com.example.showprofileactivity

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.showprofileactivity.placeholder.TimeSlot


class TimeSlotListFragment(
    private var values: MutableList<TimeSlot>

) : RecyclerView.Adapter<TimeSlotListFragment.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        println("we"+ itemCount)
        val v = LayoutInflater.from(parent.context)

            .inflate(
                if(itemCount>0) {
                    R.layout.fragment_item
                } else {
                    R.layout.empty_fragment
                },
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
            Toast.makeText(v.context, "clicked", Toast.LENGTH_SHORT)
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