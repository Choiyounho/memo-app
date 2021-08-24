package com.soten.memo.di

import com.soten.memo.ui.memolist.MemoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { MemoListViewModel(get(), get(), get()) }

}