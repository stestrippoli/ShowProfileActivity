package com.example.showprofileactivity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RateUserActivity : AppCompatActivity() {
    lateinit var ratingBar: RatingBar
    lateinit var submitButton: Button
    private lateinit var texto: TextView

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rate_user)

        ratingBar = findViewById(R.id.ratingBar)
        submitButton = findViewById(R.id.rateSubmitButton)
        texto = findViewById(R.id.textViewQl)
        submitButton.setOnClickListener(View.OnClickListener {
            texto.text = ratingBar.rating.toString()
            // Now, database information has to be updated
            // For this, we need to know the user that is being rated
        }
        )
    }
}