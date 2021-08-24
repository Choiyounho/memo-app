package com.soten.memo.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "memo")
@Parcelize
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

//    @ColumnInfo(name = "images")
//    var images: ArrayList<String>,

    @ColumnInfo(name = "createdAt")
    val createdAt: String,

    @ColumnInfo(name = "updatedAt")
    var updatedAt: String = ""
) : Parcelable