package com.mburakcakir.cryptopricetracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}