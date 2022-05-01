package com.example.showprofileactivity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.showprofileactivity.placeholder.TimeSlot
import com.example.showprofileactivity.placeholder.TimeSlotCollection
import org.json.JSONArray

/**
 * A fragment representing a list of Items.
 */
class TimeSlotFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        val objects = TimeSlotCollection
        fillItems(objects)


        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = TimeSlotListFragment(objects.ITEMS)
            }
        }
        return view
    }
    private fun fillItems(objects: TimeSlotCollection){

            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val jsonArray = JSONArray(
                //sharedPref.getString("list", "")

                """[
   {
      "id":"1",
   
         "title":"Adv 1",
         "description":"Default description",
         "location":"Default location",
         "duration":"Default duration",
         "date":"12-12-2001",
         "time":"12:00"
      
   },
   {
      "id":"2",
     
         "title":"Adv 2",
         "description":"Default description",
         "location":"Default location",
         "duration":"Default duration",
         "date":"12-12-2001",
         "time":"12:00"
      
   }
]"""
            )
            for (i in 0 until jsonArray.length()) {
                // ID
                val id = jsonArray.getJSONObject(i).getString("id")
                val title = jsonArray.getJSONObject(i).getString("title")
                val description = jsonArray.getJSONObject(i).getString("description")
                val location = jsonArray.getJSONObject(i).getString("location")
                val duration = jsonArray.getJSONObject(i).getString("duration")
                val date = jsonArray.getJSONObject(i).getString("date")
                val time = jsonArray.getJSONObject(i).getString("time")
                // Save data using your Model
                val advert = TimeSlot(title, description, location, duration, date, time)
                objects.addItem(advert)
                println("item $advert added")
            }

        }






}