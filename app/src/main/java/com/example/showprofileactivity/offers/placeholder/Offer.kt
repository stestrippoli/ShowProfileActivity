package com.example.showprofileactivity.offers.placeholder

data class Offer(
    var id: String,
    var title: String?,
                 var description: String?,
                 var location: String?,
                 var hours: Long?,
                 var creator: String?,
                 var skill: String?,
                 var email : String?,
                 var date : String?,
                 var time : String?,
                 var accepted: Boolean?,
                 var acceptedUser: String?,
                 var acceptedUserMail: String?,
                 var completed: Boolean?,
                 var ratedByCreator: Boolean?,
                 var ratedByAccepted: Boolean?
)