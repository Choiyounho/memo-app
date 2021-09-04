package com.soten.memo.domain

import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.repository.MemoRepository

class GetAllMemoListUseCase(
    private val memoRepository: MemoRepository
) {

    suspend operator fun invoke(): List<MemoEntity>? {
        return memoRepository.getAllMemo()
    }

}