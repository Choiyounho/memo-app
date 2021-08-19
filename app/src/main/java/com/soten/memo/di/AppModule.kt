package com.soten.memo.di

import com.soten.memo.data.repository.MemoRepository
import com.soten.memo.data.repository.MemoRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single<MemoRepository> { MemoRepositoryImpl() }

    single { provideDb(androidApplication()) }
    single { provideMemoDao(get()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }

}