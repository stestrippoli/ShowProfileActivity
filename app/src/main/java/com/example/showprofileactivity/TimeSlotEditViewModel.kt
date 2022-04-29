package com.example.showprofileactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.sql.Date
import java.sql.Time


class TimeSlotEditViewModel: ViewModel() {
        val title = MutableLiveData<String>().also { it.value = "Title" }
        val description = MutableLiveData<String>().also { it.value = "Description" }
        val data = MutableLiveData<String>().also { it.value = "Date" }
        val time = MutableLiveData<String>().also { it.value = "Time" }
        val duration = MutableLiveData<String>().also { it.value = "Duration" }
        val location = MutableLiveData<String>().also { it.value = "Location" }


    fun setTitle(newtitle : String) {
        title.value = newtitle
    }

    fun getTitle() : String? {
        return title.value
    }

    fun setDesc(newdesc: String){
        description.value= newdesc
    }

    fun getDesc() : String? {
        return description.value
    }

    fun setDate( newdate: String) {
        data.value = newdate
    }

    fun getDate() :String? {
        return data.value
    }

    fun setTime(newtime : String) {
        time.value = newtime
    }

    fun getTime() :String? {
        return time.value
    }

    fun setDuration(newdur : String) {
        duration.value = newdur
    }


    fun getDuration() :String? {
        return duration.value
    }

    fun setLocation(newloc : String) {
        location.value = newloc
    }

    fun getLocation() :String? {
        return location.value
    }

}