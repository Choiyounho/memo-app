package com.soten.memo.data.db.dao

import androidx.room.*
import com.soten.memo.data.db.entity.MemoEntity

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo ORDER BY updatedAt DESC")
    fun getAllMemo() : List<MemoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(memo: MemoEntity)

    @Delete
    fun deleteMemo(memo:MemoEntity)

    @Update
    fun updateMemo(memo: MemoEntity)

}