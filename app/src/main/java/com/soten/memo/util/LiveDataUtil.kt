package com.soten.memo.util

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<ArrayList<T>>.addImage(image: T) {
    val list = this.value ?: arrayListOf()
    list.add(image)
    this.value = list
}

fun <T> MutableLiveData<ArrayList<T>>.removeImage(image: T) {
    val list = this.value ?: arrayListOf()
    list.remove(image)
    this.value = list
}