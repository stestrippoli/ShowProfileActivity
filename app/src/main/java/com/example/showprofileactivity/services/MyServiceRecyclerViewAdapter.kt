package com.example.showprofileactivity.timeslots

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.showprofileactivity.R
import com.example.showprofileactivity.services.ServiceViewModel
import com.example.showprofileactivity.services.placeholder.Service
import com.example.showprofileactivity.services.placeholder.ServiceCollection
import com.example.showprofileactivity.timeslots.placeholder.TimeSlot


class MyServiceRecyclerViewAdapter(
    private var values: MutableList<Service>,
    private val vm : ServiceViewModel
) : RecyclerView.Adapter<MyServiceRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.fragment_service,
                parent,
                false
            )
        println("viewcreata")
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = vm.services.value?.get(position)
        holder.service_name.text = item?.name

        holder.itemView.setOnClickListener{v:View ->
            /*
            vm.setId(position)
            vm.setDate(item.date)
            vm.setTime(item.time)
            vm.setTitle(item.title)
            vm.setDesc(item.description)
            vm.setLocation(item.location)
            vm.setDuration(item.duration)


            v.findNavController().navigate(R.id.nav_timeSlotDetailsFragment)

             */
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val service_name: TextView



        init {

            service_name = itemView.findViewById(R.id.card_service_name)

        }

    }

}