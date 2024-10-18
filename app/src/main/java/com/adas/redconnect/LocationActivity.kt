package com.adas.redconnect

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adas.redconnect.databinding.ActivityLocationBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale
import kotlin.properties.Delegates

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var hospDbRef: DatabaseReference
    private var uid: String? = null
    private lateinit var editor:SharedPreferences.Editor
    private lateinit var binding: ActivityLocationBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val API_KEY = "AIzaSyD65Ys-rNZuaTeP1ZnrucaYWAVzNBEHgGM"
    private lateinit var myMap: GoogleMap
    private var currLat by Delegates.notNull<Double>()
    private var currLong by Delegates.notNull<Double>()
    private lateinit var searchLatlng : LatLng
    private var mm : Marker? = null
    private var hospName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid

        dbRef= FirebaseDatabase.getInstance().getReference("donor")
        hospDbRef = FirebaseDatabase.getInstance().getReference()


        if(!Places.isInitialized()){
            Places.initialize(applicationContext, API_KEY)
        }


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val autocompleteSupportFragment = supportFragmentManager.findFragmentById(R.id
            .locationSearch) as AutocompleteSupportFragment
        autocompleteSupportFragment.setPlaceFields(
                listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            )
        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Toast.makeText(this@LocationActivity, "Some Error Occured", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                searchLatlng = place.latLng!!
                binding.locationTv.setText(place.address)
                zoomOnSearchLoc()
                if(binding.locationTv.text.isNotEmpty()){
                    nextBtnEnabled()
                }
            }
        })

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.currentLocBtn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            getCurrentLocation()
        } else {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            }
        }

    }

    private fun nextBtnEnabled() {
//        dbRef.child(auth.currentUser!!.uid).child("address")
//                .setValue(binding.locationTv.text.toString())

        Log.i("locationTv", binding.locationTv.text.toString())
        binding.nxtBtn.alpha = 1.0F
        binding.nxtBtn.isClickable = true
        binding.nxtBtn.setBackgroundResource(R.drawable.btn_bg)
        nextClicked()
    }

    private fun nextClicked(){
        binding.nxtBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("ChoicePref", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            val choice = sharedPreferences.getString("choice", "donor")


            if(choice == "donor"){
                val intent = Intent(this, UserDetails::class.java)
                startActivity(intent)
                finish()
            } else{
                val i = Intent(this, HospitalMainActivity::class.java)
                hospName = intent.getStringExtra("hospitalName")!!
                hospDbRef.child("hospital").child(hospName).child("address").setValue(binding
                    .locationTv
                    .text.toString())
                editor.putBoolean("hospLoggedIn",true)
                    .apply()
                //hospDbRef.child("hospital").child(hospName).child("uid").setValue(uid)
                startActivity(i)
                finish()
            }
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        myMap = p0
    }

    private fun zoomOnSearchLoc() {
        mm?.remove()
        mm = myMap.addMarker(MarkerOptions().position(searchLatlng))
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchLatlng, 15f))
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val task: Task<Location> = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener { location ->
                if (location != null) {
                    // Use Geocoder to convert latitude/longitude to an address
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val address = addresses?.get(0)?.getAddressLine(0)
                    currLat = location.latitude
                    currLong = location.longitude


                    val latLng = LatLng(location.latitude, location.longitude)
                    mm?.remove()
                    mm = myMap.addMarker(MarkerOptions().position(latLng))
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19f))


                    // Display the address in the TextView
                    binding.locationTv.setText(address)
                    if(binding.locationTv.text.isNotEmpty()){
                        nextBtnEnabled()
                    }
                }
            }
        } else {
            // Display a message if permission is not granted
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, now get location
                getCurrentLocation()
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}