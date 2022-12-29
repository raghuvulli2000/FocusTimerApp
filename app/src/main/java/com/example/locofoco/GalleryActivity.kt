package com.example.locofoco

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager


class GalleryActivity : AppCompatActivity() {
    private lateinit var rvGallery : RecyclerView
    private lateinit var imageAdapter : ImageAdapter
    private lateinit var alertDialog : AlertDialog
    private var imagesList = mutableListOf<CatImage>()
    private var favoriteImages = mutableListOf<CatImage>()

    private var imagesShown = mutableListOf<CatImage>()
    private var isFiltered = false

    private val storageManager = StorageManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_gallery)

        rvGallery = findViewById(R.id.rvGallery)

        // load list of CatImages
        imagesList = storageManager.loadImages()
        imagesShown.addAll(imagesList)

        val onClickListener = object : ImageAdapter.OnClickListener{
            override fun onItemClicked(position: Int) {
                //remove item & notify the adapter
                launchDetailView(position)
            }
        }


        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete all images?")
            .setPositiveButton("delete",
                DialogInterface.OnClickListener { dialog, id ->
                    clearImages()
                })
            .setNegativeButton("cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        // Create the AlertDialog object and return it
        alertDialog = builder.create()

        imageAdapter = ImageAdapter(this,imagesShown,onClickListener)
        rvGallery.adapter = imageAdapter
        rvGallery.layoutManager = GridLayoutManager(this, 2)
    }

    private fun launchDetailView(position: Int) {
        // first parameter is the context, second is the class of the activity to launch
        val i = Intent(this,DetailActivity::class.java)
        i.putExtra("img", imagesShown[position])
        i.putExtra("index", position)
        getResult.launch(i)
    }

    //delete all function
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gallery,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnDeleteAll){
            alertDialog.show()
        } else if (item.itemId == R.id.btnFavorite){
            if (!isFiltered) {
                favoriteImages = getFavorite()
                Log.i(TAG, "# of favorites: "+favoriteImages.size.toString())
                imagesShown.clear()
                imagesShown.addAll(favoriteImages)
                item.setTitle("View All")
            } else {
                imagesShown.clear()
                imagesShown.addAll(imagesList)
                item.setTitle("View Favorites")
            }
            imageAdapter.notifyDataSetChanged()
            isFiltered = !isFiltered

        }
        return super.onOptionsItemSelected(item)
    }

    // delete all images in cache
    private fun clearImages(){
        imagesShown.clear()
        if (!isFiltered) { //delete all
            imagesList.clear()
        } else { //delete favorites
            imagesList.removeAll{ it.isFavorite == true }
        }
        imageAdapter.notifyDataSetChanged()
        //saveUrls()
        storageManager.saveImages(imagesList)
    }

    private fun getFavorite(): MutableList<CatImage> {
        var favorites = mutableListOf<CatImage>()
        for (i in imagesList){
            if (i.isFavorite){
                favorites.add(i)
            }
        }
        return favorites
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                if (intent != null){
                    Log.i(TAG, "Back to gallery")
                    val isDeleted= intent.getBooleanExtra("isDeleted",false)
                    val position = intent.getIntExtra("index",0)
                    if (isDeleted) {
                        if (isFiltered){
                            for (i in imagesList){
                                if (i.url == imagesShown[position].url){
                                    imagesList.remove(i)
                                    break
                                }
                            }
                        }else{
                            imagesList.removeAt(position)
                        }
                        imagesShown.removeAt(position)
                        imageAdapter.notifyItemRemoved(position)
                        storageManager.saveImages(imagesList)
                    } else {
                        val isFavorite = intent.getBooleanExtra("isFavorite",imagesList[position].isFavorite)
                        if (isFavorite != imagesShown[position].isFavorite){
                            //find the image in imagelist if isFavorite is chaned
                            for (i in imagesList){
                                if (i.url == imagesShown[position].url){
                                    if (isFiltered){//in fav page
                                        //remove image if it is not longer a fav image
                                        imagesShown.remove(i)
                                        imageAdapter.notifyItemRemoved(position)
                                    }
                                    imagesList[position].isFavorite = isFavorite
                                    break
                                }
                            }
                            storageManager.saveImages(imagesList)
                        }
                    }
                }
            }
        }


    companion object{
        private const val TAG = "Gallery"
    }
}