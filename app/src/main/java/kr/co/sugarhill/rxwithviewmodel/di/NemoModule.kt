package kr.co.sugarhill.rxwithviewmodel.di

import kr.co.sugarhill.rxwithviewmodel.view.list.ListViewModel
import kr.co.sugarhill.rxwithviewmodel.util.rx.ApplicationSchedulerProvider
import kr.co.sugarhill.rxwithviewmodel.util.rx.SchedulerProvider
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val nemoAppModule = applicationContext {
    viewModel { ListViewModel(get(), get()) }
}

val rxModule = applicationContext {
    // provided components
    bean { ApplicationSchedulerProvider() as SchedulerProvider }
}

val nemoModule = listOf(nemoAppModule, rxModule)