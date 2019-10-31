package com.app.rssfeed

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val downLoadData by lazy { DownloadData(this, xmlListView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate Called")
        downLoadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=25/xml")
//        textView.setText(file as CharSequence)
    }

    override fun onDestroy() {
        super.onDestroy()
        downLoadData.cancel(true)
    }

    companion object {
        private class DownloadData(context: Context, listView: ListView) :
            AsyncTask<String, Void, String>() {
            private val TAG = "DownLoadData"

            var dataContext: Context by Delegates.notNull()
            var dataListView: ListView by Delegates.notNull()

            init {
                dataContext = context
                dataListView = listView
            }

            override fun onProgressUpdate(vararg values: Void?) {
                super.onProgressUpdate(*values)
                Log.d(TAG, "onProgressUpdate Called")
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
//                Log.d(TAG, "onPostExecute Called $result")
                var parseXMLData = ParseXMLData()
                parseXMLData.parse(result)

                val arrayAdapter = FeedCustomAdapter(
                    dataContext,
                    R.layout.list_record,
                    parseXMLData.applications
                )
                dataListView.adapter = arrayAdapter
//
//                val arrayAdapter = ArrayAdapter<FeedEntry>(
//                    dataContext,
//                    R.layout.list_view_text,
//                    parseXMLData.applications
//                )
//                dataListView.adapter = arrayAdapter
            }

            override fun doInBackground(vararg params: String?): String {
                Log.d(TAG, "doInBackground Called ${params[0]}")
                val rssFeed = downloadXML(params[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "RSS Feed is empty")
                }
                return rssFeed
            }

            override fun onPreExecute() {
                super.onPreExecute()
                Log.d(TAG, "onPreExecute Called")
            }

//            private fun downloadXMLLongMethod(urlPath: String?): String {
//                val xmlResult = StringBuilder()
//
//                try {
//                    val url = URL(urlPath)
//                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//                    val response = connection.responseCode
//
//                    Log.d(TAG, "Response code $response")
//
//                    connection.inputStream.buffered().reader()
//                        .use { xmlResult.append(it.readText()) }
//
//                    Log.d(TAG, "Successfully got the info: ${xmlResult.length} Bytes")
//                    return xmlResult.toString()
//
//
//                } catch (e: Exception) {
//                    val errorMessage: String = when (e) {
//                        is MalformedURLException -> "Invalid URL ${e.message}"
//                        is IOException -> "IO Exception ${e.message}"
//                        is SecurityException -> {
//                            e.printStackTrace()
//                            "Internet Needed? ${e.message}"
//
//                        }
//                        else -> "Unknown error ${e.message}"
//                    }
//                    Log.e(TAG, errorMessage)
//                }
//                return "Nothing is received"
//            }

            private fun downloadXML(urlPath: String?): String {
                return URL(urlPath).readText()
            }
        }
    }
}
