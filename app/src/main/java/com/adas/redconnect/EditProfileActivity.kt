package com.adas.redconnect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adas.redconnect.databinding.ActivityEditProfileBinding
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profilePhoto: ImageView
    private lateinit var fab: FloatingActionButton

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: FirebaseStorage

    private var imageUrl: String? = null  // Store the profile image URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profilePhoto = binding.profileImage
        fab = binding.floatingActionButton

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("donor")
        storageRef = FirebaseStorage.getInstance()

//        binding.backBtn.setOnClickListener {
//            val i=Intent(this,account_InfoFragment::class.java)
//            startActivity(i)
//        }

        // Load existing profile data
        loadProfileData()

        // Image picker logic
        fab.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    // Crop image
                .compress(1024)            // Compress image
                .maxResultSize(1080, 1080) // Max resolution
                .start()
        }

        // Save button click logic
        binding.saveBtn.setOnClickListener {
            saveProfileData(imageUrl)
        }
    }

    // Load existing profile data from Firebase
    private fun loadProfileData() {
        val userId = auth.currentUser!!.uid
        dbRef.child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Set fields if data exists
                binding.tvName.setText(snapshot.child("name").value.toString())
                binding.tvBloodGrp.setText(snapshot.child("bloodgroup").value.toString())
                binding.tvGender.setText(snapshot.child("gender").value.toString())
                binding.tvAge.setText(snapshot.child("age").value.toString())
                binding.tvPhn.setText(snapshot.child("phone").value.toString())
                binding.tvLocation.setText(snapshot.child("address").value.toString())

                // Load profile image if available
                imageUrl = snapshot.child("profileUrl").value.toString()
                if (imageUrl.isNullOrEmpty()) {
                     // Load default image
                } else {
                    Glide.with(this).load(imageUrl).into(profilePhoto)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load profile data", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle image selection result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val fileUri = data.data
            profilePhoto.setImageURI(fileUri) // Preview the image locally

            // Upload image to Firebase Storage
            val userId = auth.currentUser!!.uid
            val storageReference = storageRef.reference.child("profileImages/$userId.jpg")

            storageReference.putFile(fileUri!!).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString() // Save the URL for Firebase Database
                    Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show()
                    // Save profile data after image upload
                    saveProfileData(imageUrl) // Pass the imageUrl directly
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Save profile data to Firebase
    private fun saveProfileData(imageUrl: String?) {
        val userId = auth.currentUser!!.uid
        val profileUpdates = mapOf(
            "name" to binding.tvName.text.toString(),
            "bloodGroup" to binding.tvBloodGrp.text.toString(),
            "gender" to binding.tvGender.text.toString(),
            "age" to binding.tvAge.text.toString(),
            "phone" to binding.tvPhn.text.toString(),
            "address" to binding.tvLocation.text.toString(),
            "profileUrl" to imageUrl // Use the passed imageUrl
        )

        dbRef.child(userId).updateChildren(profileUpdates).addOnSuccessListener {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
        }
    }
}
