package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adas.redconnect.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var auth: FirebaseAuth
    private var timeOutSeconds = 60L
    private lateinit var verificationCode: String
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

        auth = FirebaseAuth.getInstance()
        val phNum = intent.getStringExtra("phoneNum")
        Log.e("phoneNum", phNum.toString())
        sendOtp(phNum!!, false)

        // Set up OTP fields for automatic focus shifting
        setupOtpInputs()

        binding.NextBtn.setOnClickListener {
            val enteredOtp = binding.otpBox1.text.toString() + binding.otpBox2.text.toString() +
                    binding.otpBox3.text.toString() +
                    binding.otpBox4.text.toString() + binding.otpBox5.text.toString() + binding.otpBox6.text.toString()
            val credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp)
            signInWithPhoneAuthCredential(credential)
            setInProgress(true)
        }

        binding.resendTv.setOnClickListener {
            sendOtp(phNum, true)
        }
    }

    private fun setupOtpInputs() {
        val otpFields = arrayOf(
            binding.otpBox1, binding.otpBox2, binding.otpBox3,
            binding.otpBox4, binding.otpBox5, binding.otpBox6
        )

        otpFields.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        // Move to next EditText if available
                        if (index < otpFields.size - 1) {
                            otpFields[index + 1].requestFocus()
                        }
                    } else if (s?.isEmpty() == true && index > 0) {
                        // Move to previous EditText if backspace is pressed
                        otpFields[index - 1].requestFocus()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    fun sendOtp(phNum: String, isResend: Boolean) {
        startResendTimer()
        setInProgress(true)
        val builder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phNum)
            .setTimeout(timeOutSeconds, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(forceResendingToken).build())
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            setInProgress(false)
            signInWithPhoneAuthCredential(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(this@OtpActivity, "OTP Verification Failed", Toast.LENGTH_SHORT).show()
            setInProgress(false)
        }

        override fun onCodeSent(
            p0: String,
            p1: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(p0, p1)
            verificationCode = p0
            forceResendingToken = p1
            Toast.makeText(this@OtpActivity, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
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
                    val i = Intent(this, LocationActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(this, "OTP Verification Failed", Toast.LENGTH_SHORT).show()
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