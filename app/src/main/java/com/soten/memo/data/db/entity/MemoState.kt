package com.soten.memo.data.db.entity

sealed class MemoState {

    object NORMAL: MemoState()

    object WRITE: MemoState()

    data class READ(
        val memoEntity: MemoEntity
    ): MemoState()

    data class MODIFY(
        val memoEntity: MemoEntity
    ): MemoState()

    data class SUCCESS(
        val memoEntity: MemoEntity
    ): MemoState()

}
