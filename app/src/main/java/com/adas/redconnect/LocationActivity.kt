package com.adas.redconnect

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment

class LocationActivity : AppCompatActivity() {
    private val API_KEY = "AIzaSyD65Ys-rNZuaTeP1ZnrucaYWAVzNBEHgGM"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location)

        if(!Places.isInitialized()){
            Places.initialize(applicationContext, API_KEY)
        }

        val autocompleteSupportFragment = (supportFragmentManager.findFragmentById(R.id
            .locationSearch) as AutocompleteSupportFragment).setPlaceFields(
                listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            )

    }
}