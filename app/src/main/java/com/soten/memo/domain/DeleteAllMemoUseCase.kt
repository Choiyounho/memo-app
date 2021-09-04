package com.soten.memo.domain

import com.soten.memo.data.repository.MemoRepository
import java.io.File

class DeleteAllMemoUseCase(
    private val memoRepository: MemoRepository
){

    suspend operator fun invoke() {
        memoRepository.getAllMemo().forEach { memoList ->
            memoList.images.forEach { path ->
                val file = File(path)
                file.delete()
            }
        }
        memoRepository.deleteAll()
    }

}