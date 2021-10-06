package com.android.movies.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel(){
    protected val compositeDisposal = CompositeDisposable()

    override fun onCleared() {
        compositeDisposal.clear()
        super.onCleared()
    }
}