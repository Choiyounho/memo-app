package com.soten.memo.data.repository

import com.soten.memo.data.db.entity.MemoEntity

interface MemoRepository {

    suspend fun getAllMemo(): List<MemoEntity>

    suspend fun insertMemo(memo: MemoEntity)

    suspend fun deleteMemo(memo:MemoEntity)

    suspend fun updateMemo(memo: MemoEntity)

    suspend fun deleteAll()

}