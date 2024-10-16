package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adas.redconnect.databinding.ActivityDonorAuthBinding
import com.adas.redconnect.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var auth:FirebaseAuth
    var timeOutSeconds=60L
    private lateinit var verificationCode:String
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOtpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth=FirebaseAuth.getInstance()
        val phNum = intent.getStringExtra("phoneNum")
       Log.e("phoneNum",phNum.toString())
        sendOtp(phNum!!, false)

        binding.NextBtn.setOnClickListener {
            val enteredOtp=binding.otpBox1.text.toString()+binding.otpBox2.text.toString()+
                    binding.otpBox3.text.toString()+
                    binding.otpBox4.text.toString()+binding.otpBox5.text.toString()+binding.otpBox6.text.toString()
            val credential=PhoneAuthProvider.getCredential(verificationCode,enteredOtp)
            signInWithPhoneAuthCredential(credential)
            setInProgress(true)

        }

        binding.resendTv.setOnClickListener {
            sendOtp(phNum,true)
        }
    }

        fun sendOtp(phNum:String,isResend:Boolean){
            startResendTimer()
            setInProgress(true)
            val builder = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phNum)
                        .setTimeout(timeOutSeconds,TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)

            if(isResend){
                PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(forceResendingToken).build())
            }
            else{
                PhoneAuthProvider.verifyPhoneNumber(builder.build())
            }
        }

    private val callbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            setInProgress(false)
            signInWithPhoneAuthCredential(p0)
//            val i= Intent(this@OtpActivity,LocationActivity::class.java)
//            startActivity(i)
        }
        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(this@OtpActivity, "Otp Verification Failed", Toast.LENGTH_SHORT).show()
            setInProgress(false)

        }

        override fun onCodeSent(
            p0: String,
            p1: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(p0, p1)
            verificationCode=p0
            forceResendingToken=p1
            Toast.makeText(this@OtpActivity, "Otp Sent Successfully", Toast.LENGTH_SHORT).show()
            setInProgress(false)
        }
    }

    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.otpCircularProgressBar.visibility = View.VISIBLE
            binding.NextBtn.visibility = View.GONE
        } else {
            binding.otpCircularProgressBar.visibility = View.GONE
            binding.NextBtn.visibility = View.VISIBLE
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        setInProgress(true)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                setInProgress(false)
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithCredential:success")
                //                    val user = task.result?.user
                    val i=Intent(this,LocationActivity::class.java)
                    startActivity(i)

                } else {
                    Toast.makeText(this, "Otp Verification Failed", Toast.LENGTH_SHORT).show()
                    // Sign in failed, display a message and update the UI
                    //Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        // The verification code entered was invalid
//                    }
                    // Update UI
                }
            }
    }

    fun startResendTimer() {
        binding.resendTv.isEnabled = false
        var timeoutSeconds = 60L

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (timeoutSeconds > 0) {
                    binding.resendTv.text = "Resend OTP in $timeoutSeconds seconds"
                    timeoutSeconds--
                    handler.postDelayed(this, 1000)
                } else {
                    binding.resendTv.isEnabled = true
                    binding.resendTv.text = "Resend OTP"
                }
            }
        }
        handler.post(runnable)
    }


}