package com.example.showprofileactivity

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class TimeSlotEditFragment : Fragment(R.layout.time_slot_edit_fragment) {
    val vm by viewModels<TimeSlotEditViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.time_slot_edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<EditText>(R.id.title_e)

        val description = view.findViewById<EditText>(R.id.description_e)
        val location = view.findViewById<EditText>(R.id.location_e)
        val date = view.findViewById<DatePicker>(R.id.date_e)
        vm.data.observe(this.viewLifecycleOwner){

        }
        val time = view.findViewById<TimePicker>(R.id.time_e)
        val duration = view.findViewById<EditText>(R.id.duration_e)



    }


}