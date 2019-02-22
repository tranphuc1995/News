package com.tranphuc.news.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.tranphuc.news.R
import com.tranphuc.news.global.MethodStatic
import com.tranphuc.news.model.News
import io.realm.Realm
import io.realm.kotlin.delete
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class WebViewActivity : AppCompatActivity() {

    private var url: String = ""
    private var linkImage: String = ""
    private var title: String = ""
    private var time: String = ""
    private var realm: Realm? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_web_view)
        addControls()
        addEvents()
    }

    private fun addEvents() {
        image_back_click()
        image_bookmark_click()
    }

    private fun image_bookmark_click() {
        image_bookmark.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (checkArticleBookmarkExist(url)) {
                    Glide.with(this@WebViewActivity).load(R.drawable.ic_bookmark_not_active).into(image_bookmark)
                    deleteBookmarkArticle(url)
                } else {
                    Glide.with(this@WebViewActivity).load(R.drawable.ic_bookmark_active).into(image_bookmark)
                    bookMarkArticle(url)
                }
            }
        })
    }

    private fun deleteBookmarkArticle(url: String) {
        realm!!.executeTransactionAsync(object : Realm.Transaction {
            override fun execute(realm: Realm) {
                var news: News? = realm.where(News::class.java).equalTo("linkHtml", url).findFirst()
                news!!.deleteFromRealm()
            }
        })
    }

    private fun bookMarkArticle(url: String) {
        realm!!.executeTransactionAsync(object : Realm.Transaction {
            override fun execute(realm: Realm) {
                var news: News = realm.createObject(News::class.java)
                news.LinkImage = linkImage
                news.LinkHtml = url
                news.Time = time
                news.Title = title
            }
        }, Realm.Transaction.OnSuccess({

        }), Realm.Transaction.OnError({

        }))
    }

    private fun image_back_click() {
        image_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })
    }

    private fun addControls() {
        getDataPass()
        setupLayout()
        initWebView()
    }

    private fun getDataPass() {
        url = intent.extras.getString("url")
        linkImage = intent.extras.getString("linkImage")
        title = intent.extras.getString("title")
        time = intent.extras.getString("time")
        realm = MethodStatic.initRealm(this)
    }

    private fun initWebView() {
        webview.settings.loadsImagesAutomatically = true
        webview.settings.javaScriptEnabled = true
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
        webview.loadUrl(url)
    }

    private fun setupLayout() {
        Glide.with(this).load(R.drawable.ic_back).into(image_back)
        if (checkArticleBookmarkExist(url)) {
            Glide.with(this@WebViewActivity).load(R.drawable.ic_bookmark_active).into(image_bookmark)
        } else {
            Glide.with(this@WebViewActivity).load(R.drawable.ic_bookmark_not_active).into(image_bookmark)
        }
        textview_link.text = url
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
}
