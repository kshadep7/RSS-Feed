package com.app.rssfeed

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate Called")
        val downLoadData =  DownloadData()
        downLoadData.execute("URL is here")
    }

    companion object {
        private class DownloadData : AsyncTask<String, Void, String>(){
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
                Log.d(TAG, "doInBackground Called $params")
                return "I passed"
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
}
