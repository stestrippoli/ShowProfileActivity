package com.example.showprofileactivity.services

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.showprofileactivity.R
import com.example.showprofileactivity.services.placeholder.Service
import org.json.JSONObject

class MyServiceRecyclerViewAdapter(
    private var values: MutableList<Service>,
    private val vm : ServiceViewModel
) : RecyclerView.Adapter<MyServiceRecyclerViewAdapter.ViewHolder>() {

    private var context: Context? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.fragment_service,
                parent,
                false
            )
        context = parent.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = vm.services.value?.get(position)
        holder.service_name.text = item?.name
        holder.itemView.setOnClickListener{v:View ->

            with(holder.itemView.context.getSharedPreferences("skill_offers", Context.MODE_PRIVATE).edit()) {
                putString("skillName", item?.name)
                apply()
            }
            v.findNavController().navigate(R.id.offersFragment)
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