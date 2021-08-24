package com.soten.memo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.db.entity.MemoState
import com.soten.memo.domain.*
import kotlinx.coroutines.launch

class MemoSharedViewModel(
    private val deleteAllMemoUseCase: DeleteAllMemoUseCase,
    private val getAllMemoListUseCase: GetAllMemoListUseCase,
    private val insertMemoUseCase: InsertMemoUseCase,
    private val updateMemoUseCase: UpdateMemoUseCase,
    private val getMemoUseCase: GetMemoUseCase
) : ViewModel() {

    private val _memoListLiveData = MutableLiveData<List<MemoEntity>>()
    val memoListLiveData get() = _memoListLiveData

    private val _memoStateLiveData = MutableLiveData<MemoState>(MemoState.NORMAL)
    val memoStateLiveData get() = _memoStateLiveData

    init {
        fetch()
    }

    fun fetch() = viewModelScope.launch {
        _memoListLiveData.value = getAllMemoListUseCase()
    }

    fun insertMemo(memoEntity: MemoEntity) = viewModelScope.launch {
        insertMemoUseCase(memoEntity)
        fetch()
    }

    fun setNormalState() = viewModelScope.launch {
        _memoStateLiveData.value = MemoState.NORMAL
        fetch()
    }

    fun setReadState(memoEntity: MemoEntity) = viewModelScope.launch {
        _memoStateLiveData.value = MemoState.READ(memoEntity)
    }

    fun setModifySate(memoEntity: MemoEntity) = viewModelScope.launch {
        _memoStateLiveData.value = memoEntity.id?.let { getMemoUseCase(it)?.let { MemoState.MODIFY(it) } }
    }

    fun setWriteSate() = viewModelScope.launch {
        _memoStateLiveData.value = MemoState.WRITE
    }

    fun setSuccess(memo: MemoEntity) = viewModelScope.launch {
        _memoStateLiveData.value = MemoState.SUCCESS(memo)
    }

    fun updateMemo(memoEntity: MemoEntity) = viewModelScope.launch {
        updateMemoUseCase(memoEntity)
        fetch()
    }

    fun deleteAll() = viewModelScope.launch {
        deleteAllMemoUseCase()
        _memoListLiveData.value = listOf()
    }

}