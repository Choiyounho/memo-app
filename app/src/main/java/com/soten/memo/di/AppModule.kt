package com.soten.memo.di

import com.soten.memo.data.repository.MemoRepository
import com.soten.memo.data.repository.MemoRepositoryImpl
import com.soten.memo.domain.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single<MemoRepository> { MemoRepositoryImpl(get(), get()) }

    single { provideDb(androidApplication()) }
    single { provideMemoDao(get()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }

    factory { GetAllMemoListUseCase(get()) }
    factory { InsertMemoUseCase(get()) }
    factory { DeleteAllMemoUseCase(get()) }
    factory { UpdateMemoUseCase(get()) }
    factory { DeleteMemoUseCase(get()) }

}