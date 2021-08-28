package com.soten.memo.di

import com.soten.memo.ui.MemoSharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { MemoSharedViewModel(get(), get(), get(), get()) }

}