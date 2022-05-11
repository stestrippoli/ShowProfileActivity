package com.example.showprofileactivity.services

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.showprofileactivity.R
import com.example.showprofileactivity.services.placeholder.ServiceCollection
import com.example.showprofileactivity.timeslots.MyServiceRecyclerViewAdapter
import com.example.showprofileactivity.timeslots.placeholder.TimeSlot
import com.example.showprofileactivity.timeslots.placeholder.TimeSlotCollection
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONArray

/**
 * A fragment representing a list of Items.
 */
class ServiceFragment : Fragment() {

    private var columnCount = 2
    private val services = ServiceCollection
    private val vm by activityViewModels<ServiceViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_service_list, container, false)

        vm.services.observe(this.viewLifecycleOwner){
            services.ITEMS.clear()
            services.ITEMS.addAll(it)
        }
        val db = Firebase.firestore
        db.collection("services")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyServiceRecyclerViewAdapter(ServiceCollection.ITEMS, vm)
            }
        }
        return view
    }


}