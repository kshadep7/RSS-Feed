package com.app.rssfeed

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


private const val tag = "MainActivity"
private const val stateFeedLimit = "STATE_FEED_LIMIT"
private const val stateFeedURL = "STATE_FEED_URL"
private const val stateScrollPosition = "STATE_SCROLL_POS"

class MainActivity : AppCompatActivity() {

    private val feedViewModel: FeedViewModel by lazy {
        ViewModelProviders.of(this).get(FeedViewModel::class.java)
    }

    private var feedURL: String =
        "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var feedLimit = 10
    private var stateScroll: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(tag, "onCreate Called")

        val feedAdapter = FeedCustomAdapter(this, R.layout.list_record, Collections.emptyList())
        xmlListView.adapter = feedAdapter

        if (savedInstanceState != null) {
            feedLimit = savedInstanceState.getInt(stateFeedLimit)
            feedURL = savedInstanceState.getString(stateFeedURL).toString()

        }

        feedViewModel.feedEntries.observe(
            this,
            Observer<List<FeedEntry>> { feedEntries ->
                feedAdapter.setFeedList(
                    feedEntries ?: Collections.emptyList()
                )
            })

        feedViewModel.downLoadUrl(feedURL.format(feedLimit))
        
        Log.d(tag, "OnCreate Done")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(stateFeedURL, feedURL)
        outState.putInt(stateFeedLimit, feedLimit)
        stateScroll = xmlListView.onSaveInstanceState()
        outState.putParcelable(stateScrollPosition, stateScroll)
        Log.d(tag, "onSaveInstanceState called $feedURL, $feedLimit")
//        outState.putBoolean(isCached, flag)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feed_menu, menu)
        if (feedLimit == 10) {
            menu?.findItem(R.id.menuTop10)?.isChecked = true
        } else {
            menu?.findItem(R.id.menuTop25)?.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuFree -> feedURL =
                "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            R.id.menuPaid -> feedURL =
                "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            R.id.menuSongs -> feedURL =
                "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            R.id.menuTop10, R.id.menuTop25 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    feedLimit = 35 - feedLimit
                    Log.d(
                        tag,
                        "onOptionsItemSelected: ${item.title} setting feed limit to $feedLimit"
                    )
                } else {
                    Log.d(tag, "onOptionsItemSelected: ${item.title} setting feed limit unchanged")
                }
            }
            R.id.menuRefresh -> feedViewModel.invalidate()
            else -> return super.onOptionsItemSelected(item)
        }
        feedViewModel.downLoadUrl(feedURL.format(feedLimit))
        return super.onOptionsItemSelected(item)
    }
}
