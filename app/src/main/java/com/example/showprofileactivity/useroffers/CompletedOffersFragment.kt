package com.example.showprofileactivity.useroffers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showprofileactivity.R
import com.example.showprofileactivity.offers.OffersViewModel
import com.example.showprofileactivity.services.ServiceViewModel
import com.google.firebase.auth.FirebaseAuth

class CompletedOffersFragment: Fragment() {
    private var columnCount = 1
    private val offers = CompletedOffersCollection
    private val vm by activityViewModels<ServiceViewModel>()
    private val vmOffer by activityViewModels<OffersViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_completed_offers, container, false)

        offers.clear()
        for (offer in vm.offers.value!!)
            if (offer.accepted == true && offer.completed == true) {
                if (offer.acceptedUser == FirebaseAuth.getInstance().currentUser?.displayName || offer.creator == FirebaseAuth.getInstance().currentUser?.displayName)
                    offers.addItem(offer)
            }

        // Set the adapter
        val offersView = view.findViewById<RecyclerView>(R.id.list)
        val emptyView = view.findViewById<TextView>(R.id.empty_view)
        if (offersView is RecyclerView) {
            if (offers.count() == 0) {
                emptyView.visibility = View.VISIBLE
                offersView.visibility = View.GONE
            }
            with(offersView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyCompletedOffersRecyclerViewAdapter(CompletedOffersCollection.ITEMS, object: MyCompletedOffersRecyclerViewAdapter.ItemClickListener {
                    override fun onItemClick(position: Int) {
                        onOfferClicked(position)
                    }})
            }
        }
        return view
    }

    private fun onOfferClicked(position: Int) {
        vmOffer.setTitle(offers.ITEMS[position].title!!)
        vmOffer.setDescription(offers.ITEMS[position].description!!)
        vmOffer.setLocation(offers.ITEMS[position].location!!)
        vmOffer.setHours(offers.ITEMS[position].hours!!)
        vmOffer.setCreator(offers.ITEMS[position].creator!!)
        vmOffer.setSkill(offers.ITEMS[position].skill!!)
        vmOffer.setEmail(offers.ITEMS[position].email!!)
        vmOffer.setId(offers.ITEMS[position].id)
        vmOffer.setAccepted(offers.ITEMS[position].accepted!!)
        vmOffer.setAcceptedUser(offers.ITEMS[position].acceptedUser!!)
        vmOffer.setAcceptedUserMail(offers.ITEMS[position].acceptedUserMail!!)

        val o = Bundle()
        o.putBoolean("rated", false)
        o.putBoolean("ratedByCreator", offers.ITEMS[position].ratedByCreator!!)
        o.putBoolean("ratedByAccepted", offers.ITEMS[position].ratedByAccepted!!)


        view?.findNavController()?.navigate(R.id.action_toOfferDetailFragment, o)
    }
}