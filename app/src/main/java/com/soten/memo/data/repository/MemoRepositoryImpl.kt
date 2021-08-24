package com.soten.memo.data.repository

import com.soten.memo.data.db.dao.MemoDao
import com.soten.memo.data.db.entity.MemoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemoRepositoryImpl(
    private val memoDao: MemoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): MemoRepository {

    override suspend fun getAllMemo(): List<MemoEntity> = withContext(ioDispatcher) {
        memoDao.getAllMemo()
    }

    override suspend fun getMemo(id: Int): MemoEntity? = withContext(ioDispatcher) {
        memoDao.getMemo(id)
    }

    override suspend fun insertMemo(memo: MemoEntity) = withContext(ioDispatcher) {
        memoDao.insertMemo(memo)
    }

    override suspend fun deleteMemo(memo: MemoEntity) = withContext(ioDispatcher) {
        memoDao.deleteMemo(memo)
    }

    override suspend fun updateMemo(memo: MemoEntity) = withContext(ioDispatcher) {
        memoDao.updateMemo(memo)
    }

    override suspend fun deleteAll() {
        memoDao.deleteAll()
    }
}