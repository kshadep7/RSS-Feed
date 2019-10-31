package com.app.rssfeed

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FeedCustomAdapter(context: Context, private val resource: Int, private val applications: List<FeedEntry>) :
    ArrayAdapter<FeedEntry>(context, resource) {

    private val TAG = "FeedCustomAdapter"
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        Log.d(TAG, "getCount() called ${applications.size}")
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d(TAG, "getView() called")
        val view = inflater.inflate(resource, parent, false)

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvArtist = view.findViewById<TextView>(R.id.tvArtist)
        val tvSummary = view.findViewById<TextView>(R.id.tvSummary)

        val currentRecord = applications[position]
        tvName.text = currentRecord.name
        tvArtist.text = currentRecord.artist
        tvSummary.text = currentRecord.summary

        return view

    }
}