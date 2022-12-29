package com.example.locofoco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private var isFavorite : Boolean = false
    private lateinit var img : CatImage
    private var index : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var ivCatImage: ImageView = findViewById(R.id.ivCatImage)

        img = getIntent().getParcelableExtra("img")!!
        index = getIntent().getIntExtra("index",0)

        if (img != null) {
            Glide.with(this@DetailActivity)
                .load(img.url)
                .into(ivCatImage)
            isFavorite = img.isFavorite
        }

    }

    //send back isFavorite when going back to gallery activity
    override fun onBackPressed() {
        val data = Intent()
        data.putExtra("index", index)
        data.putExtra("isDeleted", false)
        data.putExtra("isFavorite", isFavorite)
        data.putExtra("code", 200)

        setResult(RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu,menu)

        val starIcon : MenuItem? = menu?.findItem(R.id.star_icon)

        if (starIcon != null) {
            setIsFavorite(starIcon)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.del_icon){
            val data = Intent()
            // Pass relevant data back as a result

            data.putExtra("index", index)
            data.putExtra("isDeleted", true)
            data.putExtra("code", 200) // ints work too
            // Activity finished ok, return the data
            setResult(RESULT_OK, data) // set result code and bundle data for response
            finish() // closes the activity, pass data to parent
        }
        else if (item.itemId == R.id.share_icon){
            val text = "Look at this cute cat image i found from LocoFoco!\nLink: ${img.url}"
            //intent to share the text
            val shareTxtIntent = Intent()
            shareTxtIntent.action = Intent.ACTION_SEND
            shareTxtIntent.type = "text/plain"
            shareTxtIntent.putExtra(Intent.EXTRA_TEXT, text)
            shareTxtIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out LocoFoco")
            startActivity(Intent.createChooser(shareTxtIntent, "Share via"))
        }
        else if (item.itemId == R.id.star_icon){
            isFavorite = !isFavorite
            setIsFavorite(item)
        }
        return super.onOptionsItemSelected(item)

    }

    private fun setIsFavorite(item : MenuItem){
        if (isFavorite){
            item.setIcon(android.R.drawable.btn_star_big_on)
        } else {
            item.setIcon(android.R.drawable.btn_star_big_off)
        }
    }

    companion object{
        private const val TAG = "Detail"
    }
}

