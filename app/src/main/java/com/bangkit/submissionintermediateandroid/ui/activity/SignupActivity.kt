package com.bangkit.submissionintermediateandroid.ui.activity

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.submissionintermediateandroid.R
import com.bangkit.submissionintermediateandroid.data.Result
import com.bangkit.submissionintermediateandroid.databinding.ActivitySignUpBinding
import com.bangkit.submissionintermediateandroid.ui.viewmodel.SignUpViewModel
import com.bangkit.submissionintermediateandroid.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private val signUpViewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
//        setupAction()

        binding.progressBar.visibility = View.INVISIBLE

        signUpViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            createNewUser(name, email, password)
        }


        // ini nanti diubah ke CustomView
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().length < 8) {
                    binding.passwordEditText.error = getString(R.string.password_must_8character)
                } else {
                    binding.passwordEditText.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

//    private fun setupAction() {
//
//    }

    private fun createNewUser(name: String, email: String, password: String) {
        signUpViewModel.registerUser(name, email, password).observe(this) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage(it.data.message)
                            setPositiveButton("Lanjut") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Gagal membuat akun")
                            setMessage("Email sudah dipakai")
                            setPositiveButton("Ok") { _, _ ->

                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}