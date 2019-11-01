package com.app.rssfeed

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class ViewHolder(view: View) {
    val tvName: TextView = view.findViewById(R.id.tvName)
    val tvArtist: TextView = view.findViewById(R.id.tvArtist)
    val tvSummary: TextView = view.findViewById(R.id.tvSummary)
    val tvImage: ImageView = view.findViewById(R.id.tvImage)

}

class FeedCustomAdapter(
    context: Context,
    private val resource: Int,
    private val applications: List<FeedEntry>
) :
    ArrayAdapter<FeedEntry>(context, resource) {

    private val TAG = "FeedCustomAdapter"
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        Log.d(TAG, "getCount() called ${applications.size}")
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d(TAG, "getView() called")
        val viewHolder: ViewHolder
        val view: View


//        Picasso.get().load(url).into(view)
        if (convertView != null) {
            Log.d(TAG, "Reusing view")
            view = convertView
            viewHolder = view.tag as ViewHolder
        } else {
            Log.d(TAG, "Creating new view")
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }
        val currentRecord = applications[position]
        viewHolder.tvName.text = currentRecord.name
        viewHolder.tvArtist.text = currentRecord.artist
        viewHolder.tvSummary.text = currentRecord.summary
//        viewHolder.tvImage.sou = currentRecord.summary
        Picasso.get()
            .load(currentRecord.imageURl)
            .resize(150, 150)
            .into(viewHolder.tvImage)

        return view

    }
}