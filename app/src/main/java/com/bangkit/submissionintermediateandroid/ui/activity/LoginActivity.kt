package com.bangkit.submissionintermediateandroid.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.submissionintermediateandroid.data.Result
import com.bangkit.submissionintermediateandroid.databinding.ActivityLoginBinding
import com.bangkit.submissionintermediateandroid.ViewModelFactory
import com.bangkit.submissionintermediateandroid.data.pref.UserModel
import com.bangkit.submissionintermediateandroid.data.response.LoginResponse
import com.bangkit.submissionintermediateandroid.ui.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.progressBar.visibility = View.INVISIBLE

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            userLogin(email, password)
        }
    }

    private fun processUserLogin(dataUser: LoginResponse) {
        val email = binding.emailEditText.text.toString()
        loginViewModel.saveSession(UserModel(email, dataUser.loginResult.token))
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

    private fun userLogin(email: String, password: String) {
        loginViewModel.login(email, password).observe(this) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        processUserLogin(it.data)
                        AlertDialog.Builder(this).apply {
                            setTitle("Sukses")
                            setMessage("Berhasil login")
                            setPositiveButton("Lanjut") { _, _ ->
                                ViewModelFactory.clearInstance()
                                val homeIntent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(homeIntent)
                                finish()
                                Log.e(TAG_LOGIN_ACTIVITY, "token: ${it.data.loginResult.token}")
                            }
                            create()
                            show()
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Login Gagal")
                            setMessage("Email atau Kata Sandi Salah")
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

//    private fun setupAction() {
//        binding.loginButton.setOnClickListener {
//            val email = binding.emailEditText.text.toString()
//            viewModel.saveSession(UserModel(email, "sample_token"))
//            AlertDialog.Builder(this).apply {
//                setTitle("Yeah!")
//                setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
//                setPositiveButton("Lanjut") { _, _ ->
//                    val intent = Intent(context, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(intent)
//                    finish()
//                }
//                create()
//                show()
//            }
//        }
//    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG_LOGIN_ACTIVITY = "LoginActivity"
    }
}