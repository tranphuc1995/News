package com.tranphuc.news.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.tranphuc.news.R
import com.tranphuc.news.activities.WebViewActivity
import com.tranphuc.news.model.News
import kotlinx.android.synthetic.main.item_new_header_viewpager.view.*
import kotlinx.android.synthetic.main.item_news.view.*
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import com.bumptech.glide.request.RequestOptions


class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private var listNews: MutableList<News> = ArrayList()
    private var listNewsHeader: MutableList<News> = ArrayList()
    private lateinit var context: Context
    private val handler: Handler = Handler()
    /////

    public var ListNews: MutableList<News>
        get() {
            return listNews
        }
        set(value) {
            listNews = value
        }

    public var ListNewsHeader: MutableList<News>
        get() {
            return listNewsHeader
        }
        set(value) {
            listNewsHeader = value
        }

    constructor(listNews: MutableList<News>, listNewsHeader: MutableList<News>, context: Context) : super() {
        this.listNews = listNews
        this.listNewsHeader = listNewsHeader
        this.context = context
    }

    constructor(listNews: MutableList<News>, context: Context) : super() {
        this.listNews = listNews
        this.context = context
    }


    override fun getItemViewType(position: Int): Int {
        if (listNews.get(position).Type == 0)
            return 0
        else if (listNews.get(position).Type == 1) {
            return 1
        } else if (listNews.get(position).Type == 2) {
            return 2
        } else {
            return 0;
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        var layoutInflater = LayoutInflater.from(p0?.context)
        var viewHolderCategory: View? = null
        if (viewType == 0) {
            viewHolderCategory = layoutInflater.inflate(R.layout.item_news, p0, false)
        } else if (viewType == 1) {
            viewHolderCategory = layoutInflater.inflate(R.layout.item_news_bookmark, p0, false)
        } else if (viewType == 2) {
            viewHolderCategory = layoutInflater.inflate(R.layout.item_new_header_viewpager, p0, false)
        }

        viewHolder = when (viewType) {
            0 -> ViewHolderCategory(viewHolderCategory)
            1 -> ViewHolderCategory(viewHolderCategory)
            2 -> ViewHolderHeader(viewHolderCategory)
            else -> {
                ViewHolderCategory(viewHolderCategory)
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return listNews.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, position: Int) {
        when (p0?.itemViewType) {
            0, 1 -> {
                val viewHolderCategory: ViewHolderCategory = p0 as ViewHolderCategory
                // bind data
                viewHolderCategory.setIsRecyclable(false)
                viewHolderCategory.itemView.textview_title.text = listNews.get(position).Title
                var time: String = listNews.get(position).Time
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
                viewHolderCategory.itemView.textview_time.text = time


                val requestOptions = RequestOptions()
                requestOptions.placeholder(R.color.colorItemCategoryNotActive)
                requestOptions.error(R.color.colorItemCategoryNotActive)
                Glide.with(context).setDefaultRequestOptions(requestOptions).asBitmap().load(listNews.get(position).LinkImage)
                        .into(viewHolderCategory.itemView.image_picture)

                if (p0?.itemViewType == 0) {
                    Glide.with(context).asBitmap().load(R.drawable.ic_bookmark_active).into(viewHolderCategory.itemView.image_bookmark)
                    if (listNews.get(position).IsBookMark) {
                        viewHolderCategory.itemView.image_bookmark.visibility = View.VISIBLE
                    } else {
                        viewHolderCategory.itemView.image_bookmark.visibility = View.GONE
                    }
                }
                // events
                viewHolderCategory.itemView.setOnClickListener {
                    var intent: Intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("url", listNews.get(position).LinkHtml)
                    intent.putExtra("linkImage", listNews.get(position).LinkImage)
                    intent.putExtra("title", listNews.get(position).Title)
                    intent.putExtra("time", listNews.get(position).Time)
                    context.startActivity(intent)
                }
            }
            2 -> {
                val viewHolderHeader = p0 as ViewHolderHeader
                viewHolderHeader.setIsRecyclable(false)
                Thread(Runnable {
                    var newsHeaderAdapter = NewsHeaderPagerAdapter(context, listNewsHeader)
                    handler.post(Runnable {
                        viewHolderHeader.itemView.viewpager_header_news.adapter = newsHeaderAdapter
                    })
                }).start()
            }
        }
    }

    inner class ViewHolderCategory : RecyclerView.ViewHolder {
        private lateinit var textview_title: TextView
        private lateinit var textview_time: TextView
        private lateinit var image_picture: RoundedImageView
        private lateinit var image_bookmark: ImageView

        constructor(itemView: View?) : super(itemView!!) {
            textview_title = itemView?.findViewById(R.id.textview_title) as TextView
            textview_time = itemView?.findViewById(R.id.textview_time) as TextView
            image_picture = itemView?.findViewById(R.id.image_picture) as RoundedImageView
            image_bookmark = itemView?.findViewById(R.id.image_bookmark) as ImageView
        }
    }

    inner class ViewHolderHeader : RecyclerView.ViewHolder {
        private lateinit var viewpager_header_news: ViewPager

        constructor(itemView: View?) : super(itemView!!) {
            viewpager_header_news = itemView?.findViewById(R.id.viewpager_header_news) as ViewPager
        }
    }


    ////
    private fun parseTime(time: String) {
        try {
            var formatter: DateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
            var date: Date = formatter.parse(time)
            Log.d("debug", "aaa")
        } catch (e: Exception) {
            Log.d("debug", "aaa")
        }
    }
}