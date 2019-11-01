package com.app.rssfeed

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private var feedUrlCached = "INVALIDATED"
    private val stateFeedLimit = "STATE_FEED_LIMIT"
    private val stateFeedURL = "STATE_FEED_URL"

    private val tag = "MainActivity"
    private var downLoadData: DownloadData? = null
    private var feedURL: String =
        "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var feedLimit = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(tag, "onCreate Called")

        if (savedInstanceState != null) {
            feedLimit = savedInstanceState.getInt(stateFeedLimit)
            feedURL = savedInstanceState.getString(stateFeedURL).toString()
        }
        downLoadUrl(feedURL.format(feedLimit))
        Log.d(tag, "OnCreate Done")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(stateFeedURL, feedURL)
        outState.putInt(stateFeedLimit, feedLimit)
        Log.d(tag, "onSaveInstanceState called $feedURL, $feedLimit")
//        outState.putBoolean(isCached, flag)

    }

    private fun downLoadUrl(feedURL: String) {
        if (feedURL != feedUrlCached) {
            Log.d(tag, " downLoadUrl: Starting AsyncTask")
            downLoadData = DownloadData(this, xmlListView)
            downLoadData?.execute(feedURL)
            feedUrlCached = feedURL
            Log.d(tag, "downLoadUrl done")
        } else {
            Log.d(tag, "downLoadUrl: Same data presented -> no data download")
        }
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
            R.id.menuRefresh -> feedUrlCached = "INVALIDATED"
            else -> return super.onOptionsItemSelected(item)
        }
        downLoadUrl(feedURL.format(feedLimit))
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy called")
        downLoadData?.cancel(true)
    }

    companion object {
        private class DownloadData(context: Context, listView: ListView) :
            AsyncTask<String, Void, String>() {
            private val tag = "DownLoadData AsyncTask"

            var dataContext: Context by Delegates.notNull()
            var dataListView: ListView by Delegates.notNull()

            init {
                dataContext = context
                dataListView = listView
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                Log.d(tag, "onPostExecute Called")
                val parseXMLData = ParseXMLData()
                val isParsed = parseXMLData.parse(result)

                if (isParsed) {
                    Log.d(tag, "Data parsed successfully")
                    val arrayAdapter = FeedCustomAdapter(
                        dataContext,
                        R.layout.list_record,
                        parseXMLData.applications
                    )
                    dataListView.adapter = arrayAdapter
                    Log.d(tag, "Data fed to ListView using custom array adaptor ")
                } else {
                    Log.d(tag, "Problem in parsing the data")
                }
//
//                val arrayAdapter = ArrayAdapter<FeedEntry>(
//                    dataContext,
//                    R.layout.list_view_text,
//                    parseXMLData.applications
//                )
//                dataListView.adapter = arrayAdapter
            }

            override fun doInBackground(vararg params: String?): String {
                Log.d(tag, "doInBackground Called ${params[0]}")
                val rssFeed = downloadXML(params[0])
                if (rssFeed.isEmpty()) {
                    Log.e(tag, "RSS Feed is empty")
                }
//                Log.d(tag, rssFeed)
                return rssFeed
            }

            override fun onPreExecute() {
                super.onPreExecute()
                Log.d(tag, "onPreExecute Called")
            }

/*
private fun downloadXMLLongMethod(urlPath: String?): String {
val xmlResult = StringBuilder()

try {
val url = URL(urlPath)
val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
val response = connection.responseCode

Log.d(tag, "Response code $response")

connection.inputStream.buffered().reader()
.use { xmlResult.append(it.readText()) }

Log.d(tag, "Successfully got the info: ${xmlResult.length} Bytes")
return xmlResult.toString()


} catch (e: Exception) {
val errorMessage: String = when (e) {
is MalformedURLException -> "Invalid URL ${e.message}"
is IOException -> "IO Exception ${e.message}"
is SecurityException -> {
e.printStackTrace()
"Internet Needed? ${e.message}"

}
else -> "Unknown error ${e.message}"
}
Log.e(tag, errorMessage)
}
return "Nothing is received"
}
*/

            private fun downloadXML(urlPath: String?): String {
                return URL(urlPath).readText()
            }
        }
    }
}
