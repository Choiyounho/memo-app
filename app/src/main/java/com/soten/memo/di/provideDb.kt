package com.soten.memo.di

import android.content.Context
import androidx.room.Room
import com.soten.memo.data.db.MemoDatabase

fun provideDb(context: Context): MemoDatabase =
    Room.databaseBuilder(context, MemoDatabase::class.java, MemoDatabase.DB_NAME).build()

fun provideMemoDao(memoDatabase: MemoDatabase) = memoDatabase.memoDao()