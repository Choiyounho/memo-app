package com.soten.memo.data.db.entity

sealed class MemoState {

    object NORMAL: MemoState()

    object WRITE: MemoState()

    object READ: MemoState()

    object MODIFY: MemoState()

    object SUCCESS: MemoState()

}
