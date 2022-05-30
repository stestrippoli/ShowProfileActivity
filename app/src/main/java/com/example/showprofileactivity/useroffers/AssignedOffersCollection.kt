package com.example.showprofileactivity.useroffers

import com.example.showprofileactivity.offers.placeholder.Offer
import java.util.ArrayList
import java.util.HashMap

object AssignedOffersCollection {

    val ITEMS: MutableList<Offer> = ArrayList()
    val ITEM_MAP: MutableMap<String, Offer> = HashMap()

    init {

    }

    fun addItem(item: Offer) {
        ITEMS.add(item)
        ITEM_MAP.put(item.title!!, item)
    }

    fun count():Int {
        return ITEMS.size
    }

    fun clear(){
        ITEMS.clear()
        ITEM_MAP.clear()
    }

}