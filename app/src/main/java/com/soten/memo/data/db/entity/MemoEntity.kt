package com.soten.memo.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.soten.memo.util.RoomConverters

@Entity(tableName = "memo")
@TypeConverters(RoomConverters::class)
data class MemoEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memoId")
    val id: Int? = null,

    @ColumnInfo(name = "memoTitle")
    var title: String,

    @ColumnInfo(name = "memoDescription")
    var description: String,

    @ColumnInfo(name = "isDeleted")
    var isDeleted: Boolean = false,

    @ColumnInfo(name = "images")
    val images: List<String>,

    @ColumnInfo(name = "createdAt")
    val createdAt: String,

    @ColumnInfo(name = "updatedAt")
    var updatedAt: String = "",

)