package com.tranphuc.news.activities

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.View
import com.bumptech.glide.Glide
import com.tranphuc.news.R
import com.tranphuc.news.adapter.NewsPagerAdapter

import com.tranphuc.news.fragment.NewsFragment
import com.tranphuc.news.global.MethodStatic
import com.tranphuc.news.listener.SetOnDrawerCloseCallBack
import com.tranphuc.news.model.Category
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    // list category
    private var listNameCategory: MutableList<String> = ArrayList()
    private var listCategoryParseRss: MutableList<String> = ArrayList()
    private var listCategory: MutableList<Category> = ArrayList()
    // view pager
    private lateinit var newsPagerAdapter: NewsPagerAdapter
    private var listNewsFragment: MutableList<NewsFragment> = ArrayList()

    companion object {
        private lateinit var instance: MainActivity
        public fun isInstance(): Boolean {
            return instance != null
        }

        public var Instance: MainActivity
            get() {
                return instance
            }
            set(value) {
                instance = value
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeLanguage()
        setContentView(R.layout.activity_main)
        addControls()
        addEvents();
    }

    private fun changeLanguage() {
        var locale: Locale = Locale(MethodStatic.getLanguage(this@MainActivity))
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    private fun addEvents() {
        ic_menu_click()
    }

    private fun ic_menu_click() {
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
            } else {
                drawer_layout.openDrawer(Gravity.LEFT); //OPEN Nav Drawer!
            }
        })
    }

    private fun addControls() {
        Glide.with(this).load(R.drawable.image_logo).into(imageview_logo)
        MethodStatic.createGlobal()
        MainActivity.Instance = this@MainActivity
        ////
        createDataListNameCategory()
        createCategoryParseRss()
        initViewPager()
        ////
        createDrawer()
    }

    private fun createDrawer() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        supportActionBar!!.setDisplayShowTitleEnabled(false) // ẩn tiêu đề
        ////
        var actionBarDrawerToggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
                this@MainActivity, drawer_layout, toolbar, R.string.app_name, R.string.app_name)
        drawer_layout.setDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun initViewPager() {
        newsPagerAdapter = NewsPagerAdapter(supportFragmentManager)
        addListNewsFragment()
        viewpager_news.adapter = newsPagerAdapter
        viewpager_news.offscreenPageLimit = 20
        tab_news.setupWithViewPager(viewpager_news)
        tab_news.setTabMode(TabLayout.MODE_SCROLLABLE);
        tab_news.tabRippleColor = null
    }

    private fun addListNewsFragment() {
        for (i in 0..(listCategoryParseRss.size - 1)) {
            var newsFragment: NewsFragment = NewsFragment()
            var bundle: Bundle = Bundle()
            bundle.putString("category", listCategoryParseRss.get(i))
            newsFragment.arguments = bundle
            newsPagerAdapter.addFragment(newsFragment, listNameCategory.get(i))
        }
    }

    private fun createCategoryParseRss() {
        listCategoryParseRss.add("tintuctrongngay")
        listCategoryParseRss.add("anninhhinhsu")
        listCategoryParseRss.add("thoitrang")
        listCategoryParseRss.add("taichinhbatdongsan")
        listCategoryParseRss.add("amthuc")
        listCategoryParseRss.add("canhacmtv")
        listCategoryParseRss.add("lamdep")
        listCategoryParseRss.add("phim")
        listCategoryParseRss.add("thethao")
        listCategoryParseRss.add("giaoducduhoc")
        listCategoryParseRss.add("congnghethongtin")
        listCategoryParseRss.add("oto")
    }


    private fun createDataListNameCategory() {
        val array: Array<String> = resources.getStringArray(R.array.category)
        for (i in 0..(array.size - 1)) {
            listNameCategory.add(array.get(i).toString())
        }
    }

    public fun refreshActivity() {
        finish();
        startActivity(getIntent());
    }


    public fun closeDrawer() {
        drawer_layout.closeDrawer(Gravity.LEFT);
    }
}
