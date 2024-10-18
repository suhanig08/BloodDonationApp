package com.adas.redconnect

import DonateFragment
import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


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
        val policy= StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        FirebaseMessaging.getInstance().subscribeToTopic("all_users")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Subscription to topic failed", task.exception)
                } else {
                    Log.d("FCM", "Subscribed to 'all_users' topic successfully")
                }
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(applicationContext)
                .withPermission(Manifest.permission.POST_NOTIFICATIONS).withListener(object :
                    PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()
                    }

                }).check()

        }
//        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("FCM", "Fetching FCM token failed", task.exception)
//                return@addOnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//            Log.d("FCM Token", token)
//
////             Store the token in Firestore
////            storeTokenInFirestore(token)
//        }
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