package com.soten.memo.data.db.dao

import androidx.room.*
import com.soten.memo.data.db.entity.MemoEntity

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo ORDER BY updatedAt DESC")
    suspend fun getAllMemo(): List<MemoEntity>

    @Query("SELECT * FROM memo WHERE memoId=:id")
    suspend fun getMemo(id: Int): MemoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memo: MemoEntity)

    @Delete
    suspend fun deleteMemo(memo: MemoEntity)

    @Query("DELETE FROM memo")
    suspend fun deleteAll()

    @Update
    suspend fun updateMemo(memo: MemoEntity)

}