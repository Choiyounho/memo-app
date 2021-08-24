package com.soten.memo.domain

import com.soten.memo.data.repository.MemoRepository

class DeleteAllMemoUseCase(
    private val memoRepository: MemoRepository
){

    suspend operator fun invoke() {
        memoRepository.deleteAll()
    }

}