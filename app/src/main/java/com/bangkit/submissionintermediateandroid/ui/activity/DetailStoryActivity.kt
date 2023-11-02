package com.bangkit.submissionintermediateandroid.ui.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.bangkit.submissionintermediateandroid.data.DataUser
import com.bangkit.submissionintermediateandroid.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Story"

        val dataUser = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(TAG, DataUser::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(TAG)
        }

        Glide.with(binding.root.context)
            .load(dataUser!!.imgUrl)
            .into(binding.imgItemPhoto)
        binding.tvUsername.text = dataUser.name
        binding.tvDescription.text = dataUser.description

    }

    companion object {
        const val TAG = "detailactivity"
    }
}