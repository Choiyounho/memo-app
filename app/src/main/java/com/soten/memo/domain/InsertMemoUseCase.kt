package com.soten.memo.domain

import android.util.Log
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.repository.MemoRepository

class InsertMemoUseCase(
    private val memoRepository: MemoRepository
) {

    suspend operator fun invoke(memoEntity: MemoEntity) {
        return memoRepository.insertMemo(memoEntity)
    }

}