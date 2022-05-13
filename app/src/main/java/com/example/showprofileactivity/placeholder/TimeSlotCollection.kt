package com.example.showprofileactivity.placeholder

import java.util.ArrayList
import java.util.HashMap


object TimeSlotCollection {

    val ITEMS: MutableList<TimeSlot> = ArrayList()

    val ITEM_MAP: MutableMap<Int, TimeSlot> = HashMap()

    val COUNT = ITEMS.size

    val emptyTimeSlot = TimeSlot("Insert your title here", "Insert desc", "Insert Location", "Insert duration", "01-01-2000", "12:00")
    init {
        // Add some sample items.

    }

    fun addItem(item: TimeSlot) {
        ITEMS.add(item)
        ITEM_MAP[(ITEM_MAP.size+1)] = item
    }
    fun count():Int {
        return ITEMS.size
    }

    fun clear(){
        ITEMS.clear()
        ITEM_MAP.clear()
    }


}