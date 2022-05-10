package com.example.showprofileactivity.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.showprofileactivity.R
import com.example.showprofileactivity.services.placeholder.ServiceCollection
import com.example.showprofileactivity.timeslots.MyServiceRecyclerViewAdapter

/**
 * A fragment representing a list of Items.
 */
class ServiceFragment : Fragment() {

    private var columnCount = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list2, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyServiceRecyclerViewAdapter(ServiceCollection.ITEMS)
            }
        }
        return view
    }


}