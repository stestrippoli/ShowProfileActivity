package com.example.showprofileactivity.offers

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.showprofileactivity.R
import com.example.showprofileactivity.services.ServiceViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OfferDetailFragment : Fragment() {

    private val vm by activityViewModels<OffersViewModel>()
    private val vmComp by activityViewModels<ServiceViewModel>()
    private var marked: Boolean = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offer_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = view.findViewById<TextView>(R.id.title)
        vm.title.observe(this.viewLifecycleOwner) {
            title.text = it
        }
        val description = view.findViewById<TextView>(R.id.description)
        vm.description.observe(this.viewLifecycleOwner) {
            description.text = it
        }
        val location = view.findViewById<TextView>(R.id.location)
        vm.location.observe(this.viewLifecycleOwner) {
            location.text = it
        }
        val duration = view.findViewById<TextView>(R.id.duration)
        vm.hours.observe(this.viewLifecycleOwner) {
            val hoursString = it.toString() + " hours"
            duration.text = hoursString
        }

        val o = Bundle()
        val creator = view.findViewById<TextView>(R.id.creator)
        vm.creator.observe(this.viewLifecycleOwner) {
            creator.text = it
            o.putString("uName", creator.text.toString())
        }
        vm.id.observe(this.viewLifecycleOwner) {
            o.putString("oid", it)
        }
        creator.setOnClickListener {
            val b = Bundle()
            b.putString("email", vm.email.value)
            findNavController().navigate(R.id.action_showoffercreator, b)
        }
        val button = view.findViewById<ImageButton>(R.id.chat_button)
        o.putString("cMail", vm.email.value)
        o.putLong("hours", vm.hours.value!!)
        o.putBoolean("accepted", vm.accepted.value!!)

        button.setOnClickListener { findNavController().navigate(R.id.action_offerDetailFragment_to_fragment_chat, o) }

        val markButton = view.findViewById<Button>(R.id.button)
        if(vmComp.bookmarks.value!!.contains(vm.id.value)) marked = true
        if(marked) markButton.text = getString(R.string.rem_bookmark)
        else markButton.text = getString(R.string.add_bookmark)
        markButton.setOnClickListener {
            val newList = handleBookmark(markButton)
            FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser?.email!!)
                .update("bookmarks", newList)
                .addOnSuccessListener { Log.d("Firebase", "Bookmarks successfully saved.") }
                .addOnFailureListener{ Log.d("Firebase", "Failed to save bookmarks.") }
        }
    }

    private fun handleBookmark(markButton: Button): ArrayList<String> {
        val newList = ArrayList(vmComp.bookmarks.value)
        if(marked) {
            newList.remove(vm.id.value)
            marked = false
            markButton.text = getString(R.string.add_bookmark)
        }
        else {
            newList.add(vm.id.value)
            marked = true
            markButton.text = getString(R.string.rem_bookmark)
        }
        return newList
    }
}