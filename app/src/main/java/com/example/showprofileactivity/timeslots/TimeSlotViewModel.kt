package com.example.showprofileactivity.timeslots

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class TimeSlotViewModel: ViewModel() {
        val id = MutableLiveData<Int>()
        val title = MutableLiveData<CharSequence>()
        val description = MutableLiveData<CharSequence>()
        val date = MutableLiveData<CharSequence>()
        val time = MutableLiveData<CharSequence>()
        val duration = MutableLiveData<CharSequence>()
        val location = MutableLiveData<CharSequence>()

    fun setId(idd : Int) {
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


}