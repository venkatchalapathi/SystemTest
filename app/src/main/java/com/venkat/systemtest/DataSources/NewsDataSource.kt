package com.venkat.systemtest.DataSources

import Items
import Json4Kotlin_Base
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.venkat.systemtest.Utils.NetworkService
import com.venkat.systemtest.Utils.State
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

class NewsDataSource(
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Items>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Items>
    ) {
        Log.d(TAG, "loadInitial: ")
        compositeDisposable.add(
            networkService.getNews(
                "android",
                1,
                params.requestedLoadSize
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<Json4Kotlin_Base?>() {
                    override fun onSuccess(t: Json4Kotlin_Base) {
                        Log.d(TAG, "onSuccess: ${Gson().toJson(t)}")
                        callback.onResult(
                            t.items,
                            null,
                            2
                        )
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "onSuccess: ${e.message}")
                        updateState(State.ERROR)
                    }

                }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Items>) {
        Log.d(TAG, "loadAfter: ")
        compositeDisposable.add(
            networkService.getNews(
                "android",
                params.key,
                params.requestedLoadSize
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<Json4Kotlin_Base?>() {
                    override fun onSuccess(t: Json4Kotlin_Base) {
                        Log.d(TAG, "onSuccess: ${Gson().toJson(t)}")
                        updateState(State.DONE)
                        callback.onResult(
                            t.items,
                            params.key + 1
                        )
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "onSuccess: ${e.message}")
                        updateState(State.ERROR)
                    }
                })
        )
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Items>
    ) {
        Log.d(TAG, "loadBefore: ")
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

}