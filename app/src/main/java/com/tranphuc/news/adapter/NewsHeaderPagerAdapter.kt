package com.tranphuc.news.adapter

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import com.tranphuc.news.R
import com.tranphuc.news.activities.WebViewActivity
import com.tranphuc.news.model.News

class NewsHeaderPagerAdapter() : PagerAdapter(), Parcelable {

    private lateinit var context: Context
    private lateinit var listNewsHeader: MutableList<News>

    constructor(parcel: Parcel) : this() {

    }

    constructor(context: Context, listNewsHeader: MutableList<News>) : this() {
        this.context = context
        this.listNewsHeader = listNewsHeader
    }


    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return listNewsHeader.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var v = LayoutInflater.from(context).inflate(R.layout.item_news_header, container, false)
        // find id
        var image_picture_header = v.findViewById(R.id.image_picture_header) as RoundedImageView
        var textview_title = v.findViewById(R.id.textview_title) as TextView
        var textview_time = v.findViewById(R.id.textview_time) as TextView
        // bind data
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.color.colorItemCategoryNotActive)
        requestOptions.error(R.color.colorItemCategoryNotActive)
        Glide.with(context).setDefaultRequestOptions(requestOptions)
                .asBitmap().load(listNewsHeader.get(position).LinkImage).into(image_picture_header)
        textview_title.text = listNewsHeader.get(position).Title
        var time: String = listNewsHeader.get(position).Time
        if (time.contains("minutes ago")) {
            time = time.replace("minutes ago", context.getString(R.string.minutes_ago))
        } else if (time.contains("about an hour ago")) {
            time = time.replace("about an hour ago", context.getString(R.string.about_an_hour_ago))
        } else if (time.contains("hours ago")) {
            time = time.replace("hours ago", context.getString(R.string.hours_ago))
        } else if (time.contains("yesterday")) {
            time = time.replace("yesterday", context.getString(R.string.yesterday))
        } else if (time.contains("days ago")) {
            time = time.replace("days ago", context.getString(R.string.days_ago))
        }
        textview_time.text = time
        v.setOnClickListener {
            var intent: Intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", listNewsHeader.get(position).LinkHtml)
            intent.putExtra("linkImage", listNewsHeader.get(position).LinkImage)
            intent.putExtra("title", listNewsHeader.get(position).Title)
            intent.putExtra("time", listNewsHeader.get(position).Time)
            context.startActivity(intent)
        }
        container.addView(v, 0)
        return v

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsHeaderPagerAdapter> {
        override fun createFromParcel(parcel: Parcel): NewsHeaderPagerAdapter {
            return NewsHeaderPagerAdapter(parcel)
        }

        override fun newArray(size: Int): Array<NewsHeaderPagerAdapter?> {
            return arrayOfNulls(size)
        }
    }
}