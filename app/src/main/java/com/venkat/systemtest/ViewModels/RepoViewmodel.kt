package com.venkat.systemtest.ViewModels

import Items
import Json4Kotlin_Base
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.venkat.systemtest.DataSources.NewsDataSource
import com.venkat.systemtest.DataSources.NewsDataSourceFactory
import com.venkat.systemtest.Utils.InstanceCreator
import com.venkat.systemtest.Utils.State
import io.reactivex.disposables.CompositeDisposable

class RepoViewmodel : ViewModel() {

    private val networkService = InstanceCreator.InstanceCreator();
    var newsList: LiveData<PagedList<Items>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 5
    private val newsDataSourceFactory: NewsDataSourceFactory

    init {
        newsDataSourceFactory = NewsDataSourceFactory(compositeDisposable, networkService!!)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        Log.d(TAG, "viewmodel: ")
        newsList = LivePagedListBuilder<Int, Items>(newsDataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap<NewsDataSource,
            State>(newsDataSourceFactory.newsDataSourceLiveData, NewsDataSource::state)

    fun retry() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return newsList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}