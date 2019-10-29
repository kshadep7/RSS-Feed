package com.app.rssfeed

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate Called")
        val downLoadData = DownloadData()
        downLoadData.execute("https://rss.itunes.apple.com/api/v1/us/apple-music/coming-soon/all/10/explicit.json")
//        textView.setText(file as CharSequence)
    }

    companion object {
        private class DownloadData : AsyncTask<String, Void, String>() {
            private val TAG = "AsyncTask"
            override fun onProgressUpdate(vararg values: Void?) {
                super.onProgressUpdate(*values)
                Log.d(TAG, "onProgressUpdate Called")
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG, "onPostExecute Called $result")
            }

            override fun doInBackground(vararg params: String?): String {
                Log.d(TAG, "doInBackground Called ${params[0]}")
                val rssFeed = downloadXML(params[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "RSS Feed is empty")
                }
                return rssFeed
            }

            override fun onCancelled(result: String?) {
                super.onCancelled(result)
                Log.d(TAG, "onCancelled Called")
            }

            override fun onPreExecute() {
                super.onPreExecute()
                Log.d(TAG, "onPreExecute Called")
            }

            private fun downloadXML(urlPath: String?): String {
                val xmlResult = StringBuilder()

                try {
                    val url = URL(urlPath)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    val response = connection.responseCode

                    Log.d(TAG, "Response code $response")

                    connection.inputStream.buffered().reader()
                        .use { xmlResult.append(it.readText()) }

                    Log.d(TAG, "Successfully got the info: ${xmlResult.length} Bytes")
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
                    Log.e(TAG, errorMessage)
                }
                return "Nothing is received"
            }
        }
    }
}
