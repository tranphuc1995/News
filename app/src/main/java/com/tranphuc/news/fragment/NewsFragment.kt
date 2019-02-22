package com.tranphuc.news.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.github.marlonlom.utilities.timeago.TimeAgo
import com.tranphuc.news.R
import com.tranphuc.news.activities.MainActivity
import com.tranphuc.news.adapter.NewsAdapter
import com.tranphuc.news.global.MethodStatic
import com.tranphuc.news.global.VariableStatic
import com.tranphuc.news.model.News
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_news.view.*
import org.w3c.dom.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewsFragment : Fragment() {
    private var realm: Realm? = null
    private var isLoadData = false
    // category
    private var category: String = ""
    private var position: Int = 0
    // listNews
    private var newsAdapter: NewsAdapter? = null
    private var listNews: MutableList<News> = ArrayList()
    private var listNewsHeader: MutableList<News> = ArrayList()
    private lateinit var linearLayout: RelativeLayout
    private lateinit var gridLayoutManager: GridLayoutManager
    // parse xml
    private val handler: Handler = Handler()
    private var factory: DocumentBuilderFactory? = null
    private var builder: DocumentBuilder? = null
    private var sr: StringReader? = null
    private var _is: InputSource? = null
    private var dom: Document? = null
    // load more
    private var start = 1
    private var end = 1
    private var isLoadMore = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        linearLayout = inflater?.inflate(R.layout.fragment_news, container, false) as RelativeLayout
        if (!isLoadData) {
            addControls()
            addEvents()
            isLoadData = true;
        }
        return linearLayout
    }

    private fun addEvents() {
        relative_no_wifi_click()
        refresh_content()
        recyclerview_news_scroll()
    }

    private fun recyclerview_news_scroll() {
        linearLayout.recyclerview_news.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (isLoadMore) {
                    if (gridLayoutManager.findLastVisibleItemPosition() == newsAdapter!!.itemCount - 1) {
                        notifyNewsUpdate()
                    }
                }
            }
        })
    }

    private fun refresh_content() {
        linearLayout.swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                isLoadMore = true
                start = 1
                end = 1
                callApi()
            }, 700)
        }
    }

    private fun relative_no_wifi_click() {
        linearLayout.relative_no_wifi.setOnClickListener {
            if (MainActivity.isInstance()) {
                MainActivity.Instance.refreshActivity()
            }
        }
    }

    private fun addControls() {
        initRvNews()
        if (MethodStatic.isNetworkReachable(requireContext())) {
            linearLayout.relative_no_wifi.visibility = View.GONE
            //  linearLayout.relative_loading.visibility = View.VISIBLE
            initParser()
            getDataPass()
            Handler().postDelayed({
                callApi()
            }, 500)
        } else {
            linearLayout.relative_no_wifi.visibility = View.VISIBLE
            //  linearLayout.relative_loading.visibility = View.GONE
        }
    }

    private fun initParser() {
        factory = DocumentBuilderFactory.newInstance()
        builder = factory?.newDocumentBuilder()
    }

    private fun getDataPass() {
        category = arguments!!.getString("category")
        realm = MethodStatic.initRealm(requireContext())
        Log.d("debug_name", category)
    }

    private fun callApi() {
        var getRssNews: Call<String> = VariableStatic.mService.getRss(category);
        getRssNews.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                // linearLayout.relative_loading.visibility = View.GONE
                Log.d("debug_rss", "failed")
            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.d("debug_rss", "success")
                // init adapter
                listNews.clear()
                listNewsHeader.clear()
                newsAdapter!!.ListNews.clear()
                newsAdapter!!.ListNewsHeader.clear()
                newsAdapter!!.notifyDataSetChanged()
                var news = News()  // init Layout News Header
                news.Type = 2
                listNews.add(news)
                newsAdapter!!.ListNews.add(news)
                // parse + update  data
                parseXml(response!!.body().toString())
                notifyNewsUpdate()
                //  linearLayout.relative_loading.visibility = View.GONE
                linearLayout.swipeRefreshLayout.setRefreshing(false)
            }
        })
    }

    private fun notifyNewsUpdate() {
        if (newsAdapter!!.itemCount == 1) {
            newsAdapter!!.ListNewsHeader = listNewsHeader
            newsAdapter!!.notifyItemInserted(newsAdapter!!.itemCount - 1)
        }
        // load more
        if ((end + 3) >= (listNews.size - 1)) {
            end = listNews.size - 1
            isLoadMore = false
        } else {
            end += 3
            isLoadMore = true
        }
        for (i in start..(end)) {
            newsAdapter!!.ListNews.add(listNews.get(i))
            newsAdapter!!.notifyItemInserted(newsAdapter!!.itemCount - 1)
        }
        start = end + 1
        end = start
        Log.d("debug_adapter", "adapter" + " " + newsAdapter!!.itemCount + "    listNews" + " " + listNews.size)
    }

    private fun parseXml(xml: String) {
        // init
        sr = StringReader(xml)
        _is = InputSource(sr)
        dom = builder!!.parse(_is)

        // parse rss
        var root: Element = dom!!.documentElement
        var listChannel: NodeList = root.getElementsByTagName("channel")
        var listItem: NodeList = (listChannel.item(0) as Element).getElementsByTagName("item")
        for (i in 0..(listItem.length - 1)) {
            Log.d("debug_rss_item", "" + i)
            var item: Node = listItem.item(i)
            if ((item as Element).getElementsByTagName("link").length > 0) {
                var news: News = News()
                news.Title = (item as Element).getElementsByTagName("title").item(0).textContent
                        .replace("&#34", "").replace(";", "").replace("&#39", "");
                news.LinkHtml = (item as Element).getElementsByTagName("link").item(0).textContent
                news.LinkImage = getLinkImg((item as Element).getElementsByTagName("description").item(0).textContent)
                news.Time = timeAgo((item as Element).getElementsByTagName("pubDate").item(0).textContent)
                news.Type = 0
                if (checkArticleBookmarkExist(news.LinkHtml)) {
                    news.IsBookMark = true
                } else {
                    news.IsBookMark = false
                }
                if (i >= 2) {
                    listNews.add(news)
                } else {
                    listNewsHeader.add(news)
                }
            }
        }
    }


    private fun getLinkImg(description: String): String {
        var linkImg: String = ""
        if (description.contains("src=\'")) {
            linkImg = description.split("src=\'")[1]
            linkImg = linkImg.substring(0, linkImg.lastIndexOf("' alt"))
        }
        return linkImg
    }

    private fun initRvNews() {
        gridLayoutManager = GridLayoutManager(this!!.activity, 2)
        gridLayoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(p0: Int): Int {
                if (p0 == 0) {
                    return 2
                } else {
                    return 1
                }
            }

        })

        var listNews: MutableList<News> = ArrayList()
        var listNewsHeader: MutableList<News> = ArrayList()

        linearLayout.recyclerview_news.layoutManager = gridLayoutManager as RecyclerView.LayoutManager?
        newsAdapter = NewsAdapter(listNews, listNewsHeader, requireContext())
        linearLayout.recyclerview_news.adapter = newsAdapter

    }

    private fun timeAgo(time: String): String {
        var ago: String = ""
        var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
        var date: Date = simpleDateFormat.parse(time)
        ago = TimeAgo.using(date.time)
        return ago
    }

    private fun checkArticleBookmarkExist(url: String): Boolean {
        var isArticleExist: Boolean = false
        var news: News? = null
        news = realm!!.where(News::class.java).equalTo("linkHtml", url).findFirst()
        if (news != null) {
            isArticleExist = true
        }
        return isArticleExist
    }

    override fun onResume() {
        super.onResume()
        checkBookMarkArticle()
    }

    private fun checkBookMarkArticle() {
        for (i in 0..(listNews.size - 1)) {
            if (checkArticleBookmarkExist(listNews.get(i).LinkHtml)) {
                listNews.get(i).IsBookMark = true
            } else {
                listNews.get(i).IsBookMark = false
            }
        }
        if (newsAdapter != null)
            newsAdapter!!.notifyDataSetChanged()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (view != null) {
            if (isVisibleToUser && !isLoadData) {
                addControls()
                addEvents()
                isLoadData = true
            }
        } else {
            isLoadData = false
        }
    }
}