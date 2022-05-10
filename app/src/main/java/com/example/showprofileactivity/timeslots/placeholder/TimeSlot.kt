package com.example.showprofileactivity.timeslots.placeholder

import org.json.JSONObject

class TimeSlot(newtitle:String, description : String, location: String, duration : String ,date : String,time : String) {

    var title = newtitle
    var description = description
    var location = location
    var duration = duration
    var date = date
    var time = time

    fun itemToJSON(id: Int): JSONObject {
        return JSONObject("""
        {
            "id":"$id",
            "title":"${title}",
            "description":"${description}",
            "location":"${location}",
            "duration":"${duration}",
            "date":"${date}",
            "time":"${time}"

        }
        """
        )
    }
}