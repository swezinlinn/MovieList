package com.android.movies.util

class Resource<out T> constructor(
    private val status: ResourceState,
    private val data: T?,
    private val throwable: Throwable?) {

    private val successStub: (T) -> Unit = {}
    private val loadingStub: () -> Unit = {}
    private val errorStub: (Throwable) -> Unit = {}

    val getStatus get() = status
    val getData get() = data

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(ResourceState.SUCCESS, data, null)
        }

        fun <T> error(throwable: Throwable): Resource<T> {
            return Resource(ResourceState.ERROR, null, throwable)
        }

        fun <T> loading(): Resource<T> {
            return Resource(ResourceState.LOADING, null, null)
        }
    }

    fun subscribeState(
        onLoading: (() -> Unit) = loadingStub,
        onSuccess: ((T) -> Unit) = successStub,
        onError: ((Throwable) -> Unit) = errorStub) {
        when (this.status) {
            ResourceState.LOADING -> onLoading.invoke()
            ResourceState.SUCCESS -> onSuccess.invoke(data!!)
            ResourceState.ERROR -> onError.invoke(throwable!!)
        }
    }

}