package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.adas.redconnect.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        auth=FirebaseAuth.getInstance()
        dbRef= FirebaseDatabase.getInstance().getReference("donor")


        replaceFragment(HomeFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Home ->replaceFragment(HomeFragment())
                R.id.profile ->replaceFragment(ProfileFragment())
                R.id.donate ->replaceFragment(DonateFragment())

                else->{

                }
            }
            true
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("FCM Token", token)

//             Store the token in Firestore
//            storeTokenInFirestore(token)
        }
    }
//    private fun storeTokenInFirestore(token: String) {
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val userRef = FirebaseFirestore.getInstance().collection("users").document(userId!!)
//
//        userRef.set(mapOf("fcm_token" to token), SetOptions.merge())
//            .addOnSuccessListener {
//                Log.d("Firestore", "FCM token saved successfully")
//            }
//            .addOnFailureListener { e ->
//                Log.w("Firestore", "Error saving FCM token", e)
//            }
//    }
//
    private fun replaceFragment(fragment: Fragment){



        val sharedPreferences=getSharedPreferences("DonorDet", MODE_PRIVATE)
        val name=sharedPreferences?.getString("name","")

        if(fragment==DonateFragment())
        {
            var age:String=""
            dbRef.child(name.toString()).child("age").get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    age = snapshot.value.toString()
                    Log.e("ageerror",age)
                }
            }.addOnFailureListener {
            }
//            dbRef.child(name.toString()).child("age").get().addOnSuccessListener { snapshot ->
//                if (snapshot.exists()) {
//                    val age = snapshot.value.toString()
//                }
//            }.addOnFailureListener {
//            }

            if(age.toInt()>=18)
            {
                val fragmentManager=supportFragmentManager
                val fragmentTransaction=fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameLayout,fragment)
                fragmentTransaction.commit()
            }
            else{
                val i= Intent(this,NotEligibleActivity::class.java)
                startActivity(i)
                finish()
            }
        }
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
}