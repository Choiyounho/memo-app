package com.soten.memo.domain

import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.repository.MemoRepository

class GetMemoUseCase(
    private val memoRepository: MemoRepository
) {

    suspend operator fun invoke(memoId: Int): MemoEntity? {
        return memoRepository.getMemo(memoId)
    }

}