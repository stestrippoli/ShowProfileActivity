package com.example.showprofileactivity.timeslots

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.timeslots.placeholder.TimeSlot
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration


class TimeSlotViewModel: ViewModel() {
    private val _offers = MutableLiveData<List<TimeSlot>>()
    val offers: LiveData<List<TimeSlot>> = _offers
    private val lO: ListenerRegistration
    val id = MutableLiveData<CharSequence>()
    val title = MutableLiveData<CharSequence>()
    val description = MutableLiveData<CharSequence>()
    val date = MutableLiveData<CharSequence>()
    val time = MutableLiveData<CharSequence>()
    val skill = MutableLiveData<CharSequence>()
    val creator = MutableLiveData<CharSequence>()
    val email = MutableLiveData<CharSequence>()
    val duration = MutableLiveData<CharSequence>()
    val location = MutableLiveData<CharSequence>()

    init {
        lO = FirebaseFirestore.getInstance().collection("offers")
            .addSnapshotListener{ r, e ->
                _offers.value = if (e!=null)
                    emptyList()
                else r?.mapNotNull { d -> d.toTimeslot() }
            }
    }

    private fun DocumentSnapshot.toTimeslot(): TimeSlot? {
        return try {
            val title = get("title") as String
            val description = get("description") as String
            val location = get("location") as String
            val hours = get("hours") as Long
            val creator = get("creator") as String
            val skill = get("skill") as String
            val email = get("email") as String
            val date = get("date") as String
            val time = get("time") as String
            TimeSlot(id, title, description, location, hours.toString(), creator, skill, email, date, time)
        }
        catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    fun setId(idd : String) {
        id.value = idd
    }
    fun setTitle(newtitle : CharSequence) {
        title.value = newtitle
    }

    fun setDesc(newdesc: CharSequence){
        description.value = newdesc
    }

    fun setTime(newtime : CharSequence) {
        time.value = newtime
    }

    fun setDate(newdate: CharSequence){
        date.value= newdate
    }

    fun setDuration(newdur : CharSequence) {
        duration.value = newdur
    }


    fun setLocation(newloc : CharSequence) {
        location.value = newloc
    }

    fun setSkill(skil : CharSequence) {
        skill.value = skil
    }
    fun setEmail(newloc : CharSequence) {
        email.value = newloc
    }
    fun setCreator(newloc : CharSequence) {
        creator.value = newloc
    }


}