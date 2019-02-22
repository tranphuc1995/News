package com.tranphuc.news.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.tranphuc.news.R
import com.tranphuc.news.adapter.NewsAdapter
import com.tranphuc.news.global.MethodStatic
import com.tranphuc.news.model.News
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_bookmark.*
import java.util.ArrayList

class BookmarkActivity : AppCompatActivity() {

    private lateinit var newsAdapterBookMark: NewsAdapter
    private var listNewsBookMark: MutableList<News> = ArrayList()
    ///
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
        addEvents()
    }

    private fun addEvents() {
        image_arrow_back_click()
    }

    private fun image_arrow_back_click() {
        image_arrow_back.setOnClickListener { finish() }
    }

    private fun getNewsBookMark() {
        var listNewsRealm: RealmResults<News> = realm!!.where(News::class.java).findAll()
        for (i in 0..(listNewsRealm.size - 1)) {
            var news: News = News()
            news.Type = 1;
            news.Title = listNewsRealm.get(i)!!.Title
            news.Time = listNewsRealm.get(i)!!.Time
            news.LinkImage = listNewsRealm.get(i)!!.LinkImage
            news.LinkHtml = listNewsRealm.get(i)!!.LinkHtml
            listNewsBookMark.add(news)
        }
    }

    private fun setupLayout() {
        realm = MethodStatic.initRealm(this@BookmarkActivity)
        Glide.with(this@BookmarkActivity).load(R.drawable.ic_back).into(image_arrow_back)
        listNewsBookMark.clear()
    }


    private fun initRvNewsBookMark() {
        var linearLayout: LinearLayoutManager = LinearLayoutManager(this@BookmarkActivity, LinearLayoutManager.VERTICAL, false)
        recyclerview_bookmark.layoutManager = linearLayout
        newsAdapterBookMark = NewsAdapter(listNewsBookMark, this@BookmarkActivity)
        recyclerview_bookmark.adapter = newsAdapterBookMark
    }

    override fun onResume() {
        super.onResume()
        setupLayout()
        getNewsBookMark()
        if (listNewsBookMark.size > 0) {
            relative_no_bookmark.visibility = View.GONE
        } else {
            relative_no_bookmark.visibility = View.VISIBLE
        }
        initRvNewsBookMark()
    }
}
