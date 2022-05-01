package com.example.showprofileactivity.placeholder

import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object TimeSlotCollection {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<TimeSlot> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    val ITEM_MAP: MutableMap<Int, TimeSlot> = HashMap()

    private val COUNT = ITEM_MAP.size

    init {
        // Add some sample items.

    }

    fun addItem(item: TimeSlot) {
        ITEMS.add(item)
        ITEM_MAP[(ITEM_MAP.size+1)] = item
    }

    fun clear(){
        ITEMS.clear()
        ITEM_MAP.clear()
    }



}