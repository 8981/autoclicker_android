package com.example.app_autoclicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val btnTake = findViewById<Button>(R.id.btnTake)

        btnTake.setOnClickListener {
            counter++
            tvStatus.text = "Кнопка нажата $counter раз(а)"
        }
    }
}
