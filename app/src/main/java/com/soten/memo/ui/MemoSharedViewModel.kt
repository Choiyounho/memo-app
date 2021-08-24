package com.soten.memo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.domain.DeleteAllMemoUseCase
import com.soten.memo.domain.GetAllMemoListUseCase
import com.soten.memo.domain.InsertMemoUseCase
import com.soten.memo.domain.UpdateMemoUseCase
import kotlinx.coroutines.launch

class MemoSharedViewModel(
    private val deleteAllMemoUseCase: DeleteAllMemoUseCase,
    private val getAllMemoListUseCase: GetAllMemoListUseCase,
    private val insertMemoUseCase: InsertMemoUseCase,
    private val updateMemoUseCase: UpdateMemoUseCase
) : ViewModel() {

    private val _memoListLiveData = MutableLiveData<List<MemoEntity>>()
    val memoListLiveData get() = _memoListLiveData

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

    fun deleteAll() = viewModelScope.launch {
        deleteAllMemoUseCase()
        _memoListLiveData.value = listOf()
    }

}