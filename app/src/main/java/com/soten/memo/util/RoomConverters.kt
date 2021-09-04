package com.soten.memo.util

import androidx.room.TypeConverter
import com.google.gson.Gson

class RoomConverters {

    @TypeConverter
    fun imageListToString(images: List<String>): String =
        Gson().toJson(images)

    @TypeConverter
    fun stringToImageList(imagesString: String): List<String> =
        Gson().fromJson(imagesString, arrayOf<String>().javaClass).toList()

}