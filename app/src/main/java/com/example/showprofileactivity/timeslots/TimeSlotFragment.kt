package com.example.showprofileactivity.timeslots

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.showprofileactivity.R
import com.example.showprofileactivity.timeslots.placeholder.TimeSlot
import com.example.showprofileactivity.timeslots.placeholder.TimeSlotCollection
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

/**
 * A fragment representing a list of Items.
 */
class TimeSlotFragment : Fragment() {

    private var columnCount = 1
    private val objects = TimeSlotCollection
    private val vm by activityViewModels<TimeSlotViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        fillItems(objects)
        val adapterview = view.findViewById<RecyclerView>(R.id.list)
        val emptyView = view.findViewById<TextView>(R.id.empty_view)
        if (adapterview is RecyclerView) {
            if (objects.count()==0) {
                adapterview.setVisibility(View.GONE)
                emptyView.setVisibility(View.VISIBLE)
            }
            else {
                adapterview.setVisibility(View.VISIBLE)
                emptyView.setVisibility(View.GONE)
            }
            with(adapterview) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = TimeSlotListFragment(objects.ITEMS, vm)
            }
        }

        val addbtn = view.findViewById<FloatingActionButton>(R.id.addbtn)
        addbtn.setOnClickListener {
            val empty = TimeSlotCollection.emptyTimeSlot
            vm.setId(objects.count())
            vm.setDate(empty.date)
            vm.setTime(empty.time)
            vm.setTitle(empty.title)
            vm.setDesc(empty.description)
            vm.setLocation(empty.location)
            vm.setDuration(empty.duration)
            it.findNavController().navigate(R.id.action_toEditFragment)
        }
        setHasOptionsMenu(true)
        return view
    }


    override fun onDestroyView() {
        objects.clear()
        super.onDestroyView()
    }

    private fun fillItems(objects: TimeSlotCollection){
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val jsonArray = JSONArray(
                sharedPref.getString("list", "[]")
            )

            for (i in 0 until jsonArray.length()) {
                val title = jsonArray.getJSONObject(i).getString("title")
                val description = jsonArray.getJSONObject(i).getString("description")
                val location = jsonArray.getJSONObject(i).getString("location")
                val duration = jsonArray.getJSONObject(i).getString("duration")
                val date = jsonArray.getJSONObject(i).getString("date")
                val time = jsonArray.getJSONObject(i).getString("time")
                // Save data using your Model
                val advert = TimeSlot(title, description, location, duration, date, time)
                objects.addItem(advert)
            }
        }
}