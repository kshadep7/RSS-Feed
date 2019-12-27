package com.app.rssfeed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "FeedViewModel"

class FeedViewModel : ViewModel(), DownloadData.DownloaderCallBack {

    private var downLoadData: DownloadData? = null
    private var feedUrlCached = "INVALIDATED"

    private val feed = MutableLiveData<List<FeedEntry>>()
    val feedEntries: LiveData<List<FeedEntry>>
        get() = feed

    init {
        feed.postValue(Collections.emptyList())
    }

    fun downLoadUrl(feedURL: String) {
        if (feedURL != feedUrlCached) {
            Log.d(TAG, " downLoadUrl: Starting AsyncTask")
            downLoadData = DownloadData(this)
            downLoadData?.execute(feedURL)
            feedUrlCached = feedURL
            Log.d(TAG, "downLoadUrl done")
        } else {
            Log.d(TAG, "downLoadUrl: Same data presented -> no data download")
        }
    }

    fun invalidate() {
        feedUrlCached = "INVALIDATED"
    }

    override fun onDataAvailable(data: List<FeedEntry>) {
        Log.d(TAG, "onDataAvailable starts")
        feed.value = data
        Log.d(TAG, "onDataAvailable ends")

    }

    override fun onCleared() {
        Log.d(TAG, "onCleared called: cancelling unfinished businesses")
        downLoadData?.cancel(true)
        super.onCleared()
    }
}