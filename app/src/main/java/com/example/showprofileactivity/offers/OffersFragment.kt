package com.example.showprofileactivity.offers

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView
//import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showprofileactivity.R
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.offers.placeholder.OffersCollection
import com.example.showprofileactivity.services.ServiceViewModel
import java.util.*
import kotlin.collections.ArrayList


class OffersFragment : Fragment(){

    private var columnCount = 1
    private val offers = OffersCollection
    //private val offersFiltered = OffersCollection
    private var offersFiltered: List<Offer> = ArrayList()
    private val vm by activityViewModels<ServiceViewModel>()
    private val vmOffer by activityViewModels<OffersViewModel>()
    //private val searchView = SearchView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offers_list, container, false)
        val searchView = view.findViewById<View>(R.id.search_bar) as SearchView
        val ascendingButton = view.findViewById(R.id.ascending) as Button
        val descendingButton = view.findViewById(R.id.descending) as Button

        val relatedSkill = activity?.getSharedPreferences("skill_offers", Context.MODE_PRIVATE)?.getString("skillName", "None")
        offers.clear()
        //offersFiltered.clear()
        for (offer in vm.offers.value!!)
            if (offer.skill == relatedSkill) {
                offers.addItem(offer)
            }

        val results = ArrayList<Offer>()
        for (offer in offers.ITEMS) {
            results.add(offer)
        }
        offersFiltered = results

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
                adapter = MyOffersRecyclerViewAdapter(OffersCollection.ITEMS, object: MyOffersRecyclerViewAdapter.ItemClickListener {
                    override fun onItemClick(position: Int) {
                        onOfferClicked(position)
                    }
                })

                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        filterOffers(newText, adapter as MyOffersRecyclerViewAdapter)
                        return false
                    }

                })

                ascendingButton.setOnClickListener {
                    ascendingOrder(adapter as MyOffersRecyclerViewAdapter)
                }

                descendingButton.setOnClickListener {
                    descendingOrder(adapter as MyOffersRecyclerViewAdapter)
                }
            }
        }
        //setHasOptionsMenu(true)
        return view
    }

    private fun ascendingOrder(adapter: MyOffersRecyclerViewAdapter) {
        val results = ArrayList<Offer>()
        for (offer in offersFiltered) {
            results.add(offer)
        }
        results.sortBy {
            it.hours
        }
        offersFiltered = results
        adapter.setFilteredList(offersFiltered)
    }

    private fun descendingOrder(adapter: MyOffersRecyclerViewAdapter) {
        val results = ArrayList<Offer>()
        for (offer in offersFiltered) {
            results.add(offer)
        }
        results.sortByDescending {
            it.hours
        }
        offersFiltered = results
        adapter.setFilteredList(offersFiltered)
    }

    private fun filterOffers(text: String, adapter: MyOffersRecyclerViewAdapter) {
        val results = ArrayList<Offer>()
        if (text.isNotEmpty()) {
            for (offer in offers.ITEMS) {
                val offerDesc = offer.description?.lowercase(Locale.getDefault())
                val offerTitle = offer.title?.lowercase(Locale.getDefault())
                val offerLocation = offer.location?.lowercase(Locale.getDefault())
                if (offerDesc?.contains(text) == true || offerTitle?.contains(text) == true || offerLocation?.contains(text) == true) {
                    results.add(offer)
                }
            }
        } else {
            for (offer in offers.ITEMS) {
                results.add(offer)
            }
        }
        offersFiltered = results
        adapter.setFilteredList(offersFiltered)
    }

    private fun onOfferClicked(position: Int) {
        vmOffer.setTitle(offers.ITEMS[position].title!!)
        vmOffer.setDescription(offers.ITEMS[position].description!!)
        vmOffer.setLocation(offers.ITEMS[position].location!!)
        vmOffer.setHours(offers.ITEMS[position].hours!!)
        vmOffer.setCreator(offers.ITEMS[position].creator!!)
        vmOffer.setSkill(offers.ITEMS[position].skill!!)
        vmOffer.setEmail(offers.ITEMS[position].email!!)
        vmOffer.setId(offers.ITEMS[position].id!!)
        view?.findNavController()?.navigate(R.id.action_toOfferDetailFragment)
    }

}