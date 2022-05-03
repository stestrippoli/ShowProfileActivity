package com.example.showprofileactivity.timeslots

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
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.showprofileactivity.R


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
        val title = view.findViewById<TextView>(R.id.title)
        vm.title.observe(this.viewLifecycleOwner){
            title.setText(it)
        }
        val description = view.findViewById<TextView>(R.id.description)
        vm.description.observe(this.viewLifecycleOwner){
            description.setText(it)
        }

        val location = view.findViewById<TextView>(R.id.location)
        vm.location.observe(this.viewLifecycleOwner){
            location.setText(it)
        }

        val duration = view.findViewById<TextView>(R.id.duration)
        vm.duration.observe(this.viewLifecycleOwner){
            duration.setText(it)
        }
        val date = view.findViewById<TextView>(R.id.date)
        vm.date.observe(this.viewLifecycleOwner){
            date.setText(it)
        }
        val time = view.findViewById<TextView>(R.id.time)
        vm.time.observe(this.viewLifecycleOwner){
            time.setText(it)
        }



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        view?.findNavController()?.navigate(R.id.action_toEdit)
        return true
    }


}

