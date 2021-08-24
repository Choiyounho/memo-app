package com.soten.memo.domain

import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.repository.MemoRepository

class DeleteMemoUseCase(
    private val memoRepository: MemoRepository
){

    suspend operator fun invoke(memoEntity: MemoEntity) {
        memoRepository.deleteMemo(memoEntity)
    }

}