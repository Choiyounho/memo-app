package com.soten.memo.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memoId")
    val id: Int? = null,
) : Parcelable