package com.example.showprofileactivity.offers.placeholder

import com.example.showprofileactivity.Message
import java.util.ArrayList
import java.util.HashMap

object Chat {

    val ITEMS: MutableList<Message> = ArrayList()

    init {

    }

    fun addItem(item: Message) {
        ITEMS.add(item)
    }

    fun count():Int {
        return ITEMS.size
    }

    fun clear(){
        ITEMS.clear()
    }

}