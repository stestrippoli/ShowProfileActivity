package com.example.showprofileactivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class RateUserFragment : Fragment() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    //val dbRef : DatabaseReference =  FirebaseDat.getInstance()
    lateinit var email : String
    lateinit var userBeingRated : String
    lateinit var timesRated : String
    lateinit var rating : String
    lateinit var offerId : String
    lateinit var type : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        email = FirebaseAuth.getInstance().currentUser!!.email.toString()
        userBeingRated = requireArguments().getString("userBeingRated").toString()
        offerId = requireArguments().getString("offerId").toString()
        type = requireArguments().getString("type").toString()

        rating = ""
        timesRated = ""

        db.collection("users").document(email).get()
            .addOnSuccessListener { res ->
                res.getRating()
                timesRated = res.getTimesRated()
                rating = res.getRating()
            }
            .addOnFailureListener{
                rating = "3"
                timesRated = "3"
            }
        return inflater.inflate(R.layout.rate_user, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        var submitButton = view.findViewById<Button>(R.id.rateSubmitButton)
        var texto = view.findViewById<TextView>(R.id.textViewQl)

        texto.text = "You are rating $userBeingRated"
        submitButton.setOnClickListener(View.OnClickListener {
            if (rating != "" && timesRated != "") {
                timesRated = (timesRated.toInt() + 1).toString()
                var newRating = (rating.toDouble() + ratingBar.rating).toString()
                val data = mapOf(
                    "rating" to newRating,
                    "timesrated" to timesRated
                )
                db.collection("users").document(email).update(data)
                    .addOnSuccessListener {
                        texto.text = "Success"
                }
                    .addOnFailureListener{
                        //Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                        texto.text = "Failure"

                    }
                var comment = view.findViewById<TextInputEditText>(R.id.commentText).text.toString()
                var ratedType = ""
                var commentType = ""
                if (type == "accepted") {
                    ratedType = "ratedByCreator"
                    commentType = "creatorComment"
                }
                else if (type == "creator") {
                    ratedType = "ratedByAccepted"
                    commentType = "userComment"
                }
                val data2 = mapOf(
                    ratedType to true,
                    commentType to comment
                )
                println(comment)
                db.collection("offers").document(offerId).update(data2)
                    .addOnSuccessListener {
                        texto.text = "Success"
                        findNavController().navigate(R.id.action_rateUserFragment_to_serviceFragment)
                    }
                    .addOnFailureListener{
                        //Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                        texto.text = "Failure"

                    }
            }
        })
    }

    fun DocumentSnapshot.getRating(): String {
        return get("rating") as String
    }

    fun DocumentSnapshot.getTimesRated(): String {
        return get("timesrated") as String
    }
}

