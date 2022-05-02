package com.example.showprofileactivity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import org.json.JSONArray
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireView().findNavController().navigateUp()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateBoxes()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_menu, menu)
    }

    override fun onOptionsItemSelected(sd: MenuItem): Boolean {
        val myJSON = JSONObject(this.arguments?.getString("item"))
        val  b = bundleOf("item" to myJSON.toString())
        this.view?.findNavController()?.navigate(R.id.action_toEditFragment, b)
        return super.onOptionsItemSelected(sd)
    }

    private fun populateBoxes() {
        val title = view?.findViewById<TextView>(R.id.title)
        val description = view?.findViewById<TextView>(R.id.description)
        val date = view?.findViewById<TextView>(R.id.date)
        val time = view?.findViewById<TextView>(R.id.time)
        val duration = view?.findViewById<TextView>(R.id.duration)
        val location = view?.findViewById<TextView>(R.id.location)
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        val myJSON = JSONObject(this.arguments?.getString("item"))
        title?.text = myJSON.getString("title")
        description?.text = myJSON.getString("description")
        duration?.text = myJSON.getString("duration")
        location?.text = myJSON.getString("location")
        date?.text = myJSON.getString("date")
        time?.text = myJSON.getString("time")
        vm.setId(myJSON.getString("id").toInt())
        vm.setTitle(myJSON.getString("title"))
        vm.setDesc(myJSON.getString("description"))
        vm.setDuration(myJSON.getString("duration"))
        vm.setLocation(myJSON.getString("location"))
        vm.setDate(myJSON.getString("date"))
        vm.setTime(myJSON.getString("time"))

    }
}

