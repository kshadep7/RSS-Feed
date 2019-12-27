package com.app.rssfeed

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

private const val tag = "DownLoadData AsyncTask"

class DownloadData(private val callBack: DownloaderCallBack) :
    AsyncTask<String, Void, String>() {

    interface DownloaderCallBack {
        fun onDataAvailable(data: List<FeedEntry>)
    }

    override fun onPostExecute(result: String) {
//        super.onPostExecute(result)
        Log.d(tag, "onPostExecute Called")
        val parseXMLData = ParseXMLData()
        if (result.isNotEmpty()) {
            parseXMLData.parse(result)
        }

        callBack.onDataAvailable(parseXMLData.applications)
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

    private fun downloadXML(urlPath: String?): String {
        try {
            return URL(urlPath).readText()
        } catch (e: MalformedURLException) {
            Log.d(tag, "downloadXML: Invalid URL" + e.message)
        } catch (e: SecurityException) {
            Log.d(
                tag,
                "downloadXML: Security Exception --> Needs Internet Permission!!" + e.message
            )
        } catch (e: IOException) {
            Log.d(tag, "downloadXML: IO Exception" + e.message)
        }

        return ""       // Return an Empty String if any exception occurred.
    }
}
