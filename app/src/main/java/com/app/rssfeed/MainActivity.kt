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

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate Called")
        val downLoadData = DownloadData()
        downLoadData.execute("URL is here")
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
        }
    }

    private fun downloadXML(urlPath: String?): String {
        val xmlResult = StringBuilder()

        try {
            val url = URL(urlPath)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val response = connection.responseCode

            Log.d(TAG, "Response code $response")
//            val inputStream = connection.inputStream
//            val inputStreamReader = InputStreamReader(inputStream)
//            val reader = BufferedReader(inputStreamReader)

            val reader = BufferedReader(InputStreamReader(connection.inputStream))

            val inputBuffer = CharArray(500)
            var charRead = 0
            while (charRead>=0){
                charRead = reader.read(inputBuffer)
                if (charRead>0){
                    xmlResult.append(String(inputBuffer,0,charRead))
                }
            }

            reader.close()

            Log.d(TAG, "Successfully got the info: ${xmlResult.length} Bytes")
            return xmlResult.toString()

        } catch (e: MalformedURLException) {
            Log.e(TAG, e.message!!)
        } catch (e: IOException) {
            Log.e(TAG, e.message!!)
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        }
    }
}
