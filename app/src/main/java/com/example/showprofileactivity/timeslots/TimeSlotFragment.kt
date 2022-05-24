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
import com.example.showprofileactivity.offers.OffersViewModel
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.offers.placeholder.OffersCollection
import com.example.showprofileactivity.services.ServiceViewModel
import com.example.showprofileactivity.timeslots.placeholder.TimeSlot
import com.example.showprofileactivity.timeslots.placeholder.TimeSlotCollection
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

/**
 * A fragment representing a list of Items.
 */
class TimeSlotFragment : Fragment() {

    private var columnCount = 1
    private val objects = OffersCollection
    private val vm by activityViewModels<ServiceViewModel>()
    private val vmts by activityViewModels<TimeSlotViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        for (offer in vm.offers.value!!)
            if (offer.email == requireActivity().intent.getBundleExtra("user")?.getString("email")) {
                objects.addItem(offer)
            }


        val adapterview = view.findViewById<RecyclerView>(R.id.list)
        val emptyView = view.findViewById<TextView>(R.id.empty_view)
        if (adapterview is RecyclerView) {
            if (objects.count()==0) {
                adapterview.setVisibility(View.GONE)
                emptyView.setVisibility(View.VISIBLE)
            }
            with(adapterview) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = TimeSlotListFragment(objects.ITEMS, vmts)
            }
        }

        val addbtn = view.findViewById<FloatingActionButton>(R.id.addbtn)
        addbtn.setOnClickListener {
            val empty = TimeSlotCollection.emptyTimeSlot
            vmts.setSkill(empty.skill)
            vmts.setId("o"+ vm.offers.value!!.size.plus(1))
            vmts.setDate(empty.date)
            vmts.setTime(empty.time)
            vmts.setTitle(empty.title)
            vmts.setDesc(empty.description)
            vmts.setLocation(empty.location)
            vmts.setDuration(empty.duration)
            requireActivity().intent.getBundleExtra("user")?.getString("fullname")
                ?.let { it1 -> vmts.setCreator(it1) }
            requireActivity().intent.getBundleExtra("user")?.getString("email")
                ?.let { it1 -> vmts.setEmail(it1) }


            it.findNavController().navigate(R.id.action_toEditFragment)
        }
        setHasOptionsMenu(true)
        return view
    }


    override fun onDestroyView() {
        objects.clear()
        super.onDestroyView()
    }

}