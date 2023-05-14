package com.example.androidapp.roomDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*


@Entity(tableName = "app_tbl")
data class NewsModel(

    @PrimaryKey
    val id:String=UUID.randomUUID().toString(),
    @ColumnInfo(name = "source"      ) var source      : String,
    @ColumnInfo(name = "author"      ) var author      : String,
    @ColumnInfo(name = "title"       ) var title       : String,
    @ColumnInfo(name = "description" ) var description : String,
    @ColumnInfo(name = "url"         ) var url         : String,
    @ColumnInfo(name = "urlToImage"  ) var urlToImage  : String,
    @ColumnInfo(name = "publishedAt" ) var publishedAt : String,
    @ColumnInfo(name = "content"     ) var content     : String

)