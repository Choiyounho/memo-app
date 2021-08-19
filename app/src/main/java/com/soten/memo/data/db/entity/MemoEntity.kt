package com.soten.memo.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class MemoEntity(
    val title: String
) : Parcelable