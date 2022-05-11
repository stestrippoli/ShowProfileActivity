package com.example.showprofileactivity.services.placeholder

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object ServiceCollection {


    val ITEMS: MutableList<Service> = ArrayList()
    val ITEM_MAP: MutableMap<String, Service> = HashMap()

    private val COUNT = 25


    private fun addItem(item: Service) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

}

class Service(id: String, name: String, users: List<String>) {
    val id = id
    val name = name
    val users = users
}