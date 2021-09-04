package com.soten.memo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soten.memo.data.db.dao.MemoDao
import com.soten.memo.data.db.entity.MemoEntity

@Database(
    entities = [MemoEntity::class],
    version = 3,
    exportSchema = false
)
abstract class MemoDatabase : RoomDatabase() {

    abstract fun memoDao(): MemoDao

    companion object {
        const val DB_NAME = "memo.db"
    }

}