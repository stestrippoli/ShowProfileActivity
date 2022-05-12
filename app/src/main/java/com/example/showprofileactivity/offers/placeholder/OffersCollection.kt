package com.example.showprofileactivity.offers.placeholder

import java.util.ArrayList
import java.util.HashMap

object OffersCollection {

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