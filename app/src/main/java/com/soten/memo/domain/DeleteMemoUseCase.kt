package com.soten.memo.domain

import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.repository.MemoRepository
import java.io.File

class DeleteMemoUseCase(
    private val memoRepository: MemoRepository,
) {

    suspend operator fun invoke(memoEntity: MemoEntity) {
        memoEntity.images.forEach { path ->
            val file = File(path)
            file.delete()
        }
        memoRepository.deleteMemo(memoEntity)
    }

}