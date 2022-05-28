package com.example.showprofileactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class IntroActivity : AppCompatActivity() {
    lateinit var googleSignIn: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    //private lateinit var database: DatabaseReference
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                if (task.isSuccessful) {
                    try {
                        val account = task.getResult(ApiException::class.java)
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        auth = Firebase.auth
        //database = Firebase.database.reference

        //val rating = db.collection("users").document()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.clientId))
            .requestEmail()
            .build()

        googleSignIn = GoogleSignIn.getClient(this, gso)

        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
            resultLauncher.launch(googleSignIn.signInIntent)
        }
    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser != null) resultLauncher.launch(googleSignIn.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val account: FirebaseUser? = FirebaseAuth.getInstance().currentUser

                    var dbUser: Map<String, Any>
                    db.collection("users").document(account?.email!!)
                        .get().addOnSuccessListener { document ->
                            if(document.data == null)
                                db.collection("users").document(account.email!!)
                                    .set(User(account.displayName!!, "Your Username", "Your Location", "", "Your Description"))
                                    .addOnSuccessListener { Log.d("Firebase", "User successfully added to db") }
                                    .addOnFailureListener{ Log.d("Firebase", "Failed to add user") }
                        }

                    Toast.makeText(this, "Successfully logged in as " + account.displayName, Toast.LENGTH_SHORT).show()
                    var b = Bundle()
                    b.putString("fullname", account.displayName)
                    b.putString("email", account.email)
                    var i = Intent(this, MainActivity::class.java)
                    i.putExtra("user", b)
                    startActivity(i)
                    finish()
                }
                else Toast.makeText(this, "Failed to sign in, please retry.", Toast.LENGTH_SHORT).show()
            }
    }
}