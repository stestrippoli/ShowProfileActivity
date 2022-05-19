package com.example.showprofileactivity.timeslots

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.showprofileactivity.R
import com.example.showprofileactivity.offers.OffersViewModel
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.services.ServiceViewModel
import com.example.showprofileactivity.timeslots.placeholder.TimeSlot


class TimeSlotListFragment(
    private var values: MutableList<Offer>,
    private var vm: TimeSlotViewModel

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
            vm.setId(item.id)
            vm.setCreator(item.creator.toString())
            vm.setEmail(item.email.toString())
            vm.setSkill(item.skill.toString())
            vm.setDate(item.date.toString())
            vm.setTime(item.time.toString())
            vm.setTitle(item.title.toString())
            vm.setDesc(item.description.toString())
            vm.setLocation(item.location.toString())
            vm.setDuration(item.hours.toString())
            v.findNavController().navigate(R.id.nav_timeSlotDetailsFragment)
        }

        val btn = holder.itemView.findViewById<Button>(R.id.edit_btn)
        btn.setOnClickListener{
            vm.setId(item.id)
            vm.setCreator(item.creator.toString())
            vm.setEmail(item.email.toString())
            vm.setSkill(item.skill.toString())
            vm.setDate(item.date.toString())
            vm.setTime(item.time.toString())
            vm.setTitle(item.title.toString())
            vm.setDesc(item.description.toString())
            vm.setLocation(item.location.toString())
            vm.setDuration(item.hours.toString())
            it.findNavController().navigate(R.id.action_toEditFragment)
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