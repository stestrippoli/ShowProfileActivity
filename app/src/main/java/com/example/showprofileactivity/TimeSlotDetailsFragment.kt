package com.example.showprofileactivity

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import org.json.JSONObject


class TimeSlotDetailsFragment : Fragment(R.layout.fragment_time_slot_details) {
    val vm by activityViewModels<TimeSlotViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_time_slot_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateBoxes()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        )
                || super.onOptionsItemSelected(item)
    }

    private fun populateBoxes() {
        val title = view?.findViewById<TextView>(R.id.title)
        val description = view?.findViewById<TextView>(R.id.description)
        val date = view?.findViewById<TextView>(R.id.date)
        val time = view?.findViewById<TextView>(R.id.time)
        val duration = view?.findViewById<TextView>(R.id.duration)
        val location = view?.findViewById<TextView>(R.id.location)
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        val myJSON = JSONObject(
            sharedPref.getString(
                "timeslot",
                """{"title":"Default title","description":"Default description","location":"Default location","duration":"Default duration","date":"12-12-2001","time":"12:00"}"""
            )
        )

        title?.text = myJSON.getString("title")
        description?.text = myJSON.getString("description")
        duration?.text = myJSON.getString("duration")
        location?.text = myJSON.getString("location")
        date?.text = myJSON.getString("date")
        time?.text = myJSON.getString("time")

        vm.setTitle(myJSON.getString("title"))
        vm.setDesc(myJSON.getString("description"))
        vm.setDuration(myJSON.getString("duration"))
        vm.setLocation(myJSON.getString("location"))
        vm.setDate(myJSON.getString("date"))
        vm.setTime(myJSON.getString("time"))

    }
}

