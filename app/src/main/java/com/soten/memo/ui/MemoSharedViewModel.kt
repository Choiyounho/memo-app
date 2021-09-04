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
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {

    private val _memoListLiveData = MutableLiveData<List<MemoEntity>>()
    val memoListLiveData get() = _memoListLiveData

    private val _memoStateLiveData = MutableLiveData<MemoState>(MemoState.NORMAL)
    val memoStateLiveData get() = _memoStateLiveData

    private val _memoEntityLiveData = MutableLiveData<MemoEntity?>()
    val memoEntityLiveData get() = _memoEntityLiveData

    private val _imagePathLiveData = MutableLiveData<ArrayList<String>>()
    val imagePathLiveData get() = _imagePathLiveData

    init {
        fetch()
    }

    private fun fetch() = viewModelScope.launch {
        _memoListLiveData.value = getAllMemoListUseCase() ?: listOf()
        _imagePathLiveData.value?.clear()
    }

    fun insertMemo(memoEntity: MemoEntity) = viewModelScope.launch {
        insertMemoUseCase(memoEntity)
        fetch()
    }

    fun setMemoEntity(memoEntity: MemoEntity) = viewModelScope.launch {
        _memoEntityLiveData.value = memoEntity
    }

    fun setNormalState() = viewModelScope.launch {
        _memoStateLiveData.value = MemoState.NORMAL
        fetch()
    }

    fun setReadState() = viewModelScope.launch {
        _memoStateLiveData.value = MemoState.READ
    }

    fun setModifySate() = viewModelScope.launch {
        _memoStateLiveData.value = MemoState.MODIFY
    }

    fun setWriteSate() = viewModelScope.launch {
        _memoStateLiveData.value = MemoState.WRITE
    }

    fun setSuccess() = viewModelScope.launch {
        _memoStateLiveData.value = MemoState.SUCCESS
    }

    fun updateMemo(memoEntity: MemoEntity) = viewModelScope.launch {
        updateMemoUseCase(memoEntity)
        fetch()
    }

    fun delete(memoEntity: MemoEntity) = viewModelScope.launch {
        deleteMemoUseCase(memoEntity)
    }

    fun deleteAll() = viewModelScope.launch {
        deleteAllMemoUseCase()
        _memoListLiveData.value = listOf()
    }

}