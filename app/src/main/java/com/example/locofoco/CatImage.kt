package com.example.locofoco

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CatImage(val url : String, var isFavorite : Boolean = false) : Parcelable {

}