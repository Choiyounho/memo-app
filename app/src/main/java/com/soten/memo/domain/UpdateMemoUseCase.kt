package com.soten.memo.domain

import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.repository.MemoRepository

class UpdateMemoUseCase(
    private val memoRepository: MemoRepository
) {

    suspend operator fun invoke(memoEntity: MemoEntity) {
        memoRepository.updateMemo(memoEntity)
    }

}