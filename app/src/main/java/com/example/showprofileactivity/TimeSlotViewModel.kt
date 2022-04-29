package com.example.showprofileactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class TimeSlotViewModel: ViewModel() {


        val cal = Calendar.DATE

        val title = MutableLiveData<CharSequence>().also { it.value = "Default Title" }
        val description = MutableLiveData<CharSequence>().also { it.value = "Default Description" }
        val date = MutableLiveData<CharSequence>().also { it.value = "01-01-2000"}
        val time = MutableLiveData<CharSequence>().also { it.value = "12:00" }
        val duration = MutableLiveData<CharSequence>().also { it.value = "Default Duration" }
        val location = MutableLiveData<CharSequence>().also { it.value = "Default Location" }

    fun setTitle(newtitle : CharSequence) {
        title.setValue(newtitle)
        println("setted "+title.value)
    }

    fun setDesc(newdesc: CharSequence){
        description.value= newdesc
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


}