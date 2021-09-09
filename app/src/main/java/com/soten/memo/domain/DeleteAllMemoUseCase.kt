package com.soten.memo.domain

import com.soten.memo.data.repository.MemoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class DeleteAllMemoUseCase(
    private val memoRepository: MemoRepository,
) {

    suspend operator fun invoke() {
        val filePathList = ArrayList<String>()
        memoRepository.getAllMemo().forEach { memoEntity ->
            filePathList.addAll(memoEntity.images)
        }
        filePathList.forEach { path ->
            val file = File(path)
            file.delete()
        }
        memoRepository.deleteAll()
    }

}