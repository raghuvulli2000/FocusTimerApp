package com.example.locofoco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class PopUpWindow : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_window)

        // Get the data
        val imageView = findViewById<ImageView>(R.id.imageView_popup)
        var imgUrl = intent.extras?.getString("img_url", "url") ?: ""
        Log.i(TAG, "img_url: $imgUrl")

        // Set the data
        findViewById<TextView>(R.id.popup_window_title).text = "Here is a new friend I found!"
        findViewById<Button>(R.id.btn_home).text = "Back"
        Glide.with(this@PopUpWindow)
            .load(imgUrl)
            .into(imageView)

        findViewById<Button>(R.id.btn_home).setOnClickListener {
            finish()
        }

        //share the data
        findViewById<Button>(R.id.btn_share).setOnClickListener{
            val text = "Look at this cute cat image I found from LocoFoco!\nLink: $imgUrl"
            //intent to share the text
            val shareTxtIntent = Intent()
            shareTxtIntent.action = Intent.ACTION_SEND
            shareTxtIntent.type = "text/plain"
            shareTxtIntent.putExtra(Intent.EXTRA_TEXT, text)
            shareTxtIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out LocoFoco")
            startActivity(Intent.createChooser(shareTxtIntent, "Share via"))
        }
    }

    companion object{
        private const val TAG = "PopUpWindow"
    }

}