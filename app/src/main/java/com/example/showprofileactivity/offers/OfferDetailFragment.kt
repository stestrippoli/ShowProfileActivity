package com.example.showprofileactivity.offers

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.showprofileactivity.R
import com.example.showprofileactivity.services.ServiceViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class OfferDetailFragment : Fragment() {

    private val vm by activityViewModels<OffersViewModel>()
    private val vmComp by activityViewModels<ServiceViewModel>()
    private var marked: Boolean = false
    lateinit var currentUserEmail : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        currentUserEmail = FirebaseAuth.getInstance().currentUser?.email!!
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

        val button = view.findViewById<ImageButton>(R.id.chat_button)
        val o = Bundle()
        val creator = view.findViewById<TextView>(R.id.creator)
        vm.creator.observe(this.viewLifecycleOwner) {
            creator.text = it
            o.putString("uName", creator.text.toString())
        }
        vm.id.observe(this.viewLifecycleOwner) {
            o.putString("oid", it)
        }
        vm.email.observe(this.viewLifecycleOwner) {
            if(it==currentUserEmail)
                button.visibility = View.GONE
        }
        creator.setOnClickListener {
            val b = Bundle()
            b.putString("email", vm.email.value)
            findNavController().navigate(R.id.action_showoffercreator, b)
        }

        o.putString("cMail", vm.email.value)
        o.putLong("hours", vm.hours.value!!)
        o.putBoolean("accepted", vm.accepted.value!!)
        o.putString("oTitle", vm.title.value!!)

        button.setOnClickListener { findNavController().navigate(R.id.action_offerDetailFragment_to_fragment_chat, o) }

        val rateButton = view.findViewById<Button>(R.id.rateUserButton)
        rateButton.visibility = View.GONE
        val ll = view.findViewById<LinearLayout>(R.id.commentLayout)
        ll.visibility = View.GONE
        val commentLab = view.findViewById<TextView>(R.id.commentLabel)
        val comment = view.findViewById<TextView>(R.id.comment)
        var emailBeingRated = ""
        var userBeingRated = ""
        var type = ""
        if (!requireArguments().getBoolean("rated")) {
            if (currentUserEmail == vm.acceptedUserMail.value.toString() && !requireArguments().getBoolean("ratedByAccepted")) {
                rateButton.visibility = View.VISIBLE
                emailBeingRated = vm.email.value.toString()
                userBeingRated = vm.creator.value.toString()
                type = "creator"
            }
            else if (currentUserEmail == vm.email.value.toString() && !requireArguments().getBoolean("ratedByCreator")) {
                rateButton.visibility = View.VISIBLE
                emailBeingRated = vm.acceptedUserMail.value.toString()
                userBeingRated = vm.acceptedUser.value.toString()
                type = "accepted"
            }
        }
        if (currentUserEmail == vm.acceptedUserMail.value.toString() && requireArguments().getBoolean("ratedByCreator") && vm.creatorComment.value.toString()!=""){
            ll.visibility = View.VISIBLE
            commentLab.text = "${vm.creator.value.toString()} commented:"
            comment.text = vm.creatorComment.value.toString()
        }
        else if (currentUserEmail == vm.email.value.toString() && requireArguments().getBoolean("ratedByAccepted") && vm.userComment.value.toString()!="") {
            ll.visibility = View.VISIBLE
            commentLab.text = "${vm.acceptedUser.value.toString()} commented:"
            comment.text = vm.userComment.value.toString()
        }

        val completeButton = view.findViewById<Button>(R.id.completeOffer)
        completeButton.visibility = View.GONE
        if (currentUserEmail == vm.acceptedUserMail.value.toString() && !requireArguments().getBoolean("completed")) {
            completeButton.visibility = View.VISIBLE
        }
        completeButton.setOnClickListener {
            FirebaseFirestore.getInstance().collection("offers").document(vm.id.value.toString())
                .update("completed", true)
                .addOnSuccessListener {
                    Log.d("Firebase", "Offer successfully set as completed.")
                    completeButton.visibility = View.GONE
                    rateButton.visibility = View.VISIBLE
                }
                .addOnFailureListener{ Log.d("Firebase", "Could not set offer as completed.") }
        }


        val usersData = Bundle()
        usersData.putString("type", type)
        usersData.putString("email", emailBeingRated)
        usersData.putString("userBeingRated", userBeingRated)
        usersData.putString("offerId", vm.id.value)
        rateButton.setOnClickListener{
            findNavController().navigate(R.id.action_offerDetailFragment_to_rateUserFragment, usersData)
        }

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