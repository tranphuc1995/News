package com.tranphuc.news.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.view.*
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.tranphuc.news.R
import com.tranphuc.news.activities.BookmarkActivity
import com.tranphuc.news.activities.MainActivity
import com.tranphuc.news.global.MethodStatic
import com.tranphuc.news.listener.SetOnDrawerCloseCallBack
import kotlinx.android.synthetic.main.dialog_language.*
import kotlinx.android.synthetic.main.fragment_drawer.view.*
import java.lang.Exception
import java.lang.reflect.Method
import java.util.*

@Suppress("DEPRECATION")
class DrawerFragment : Fragment() {

    private lateinit var linearLayout: LinearLayout
    private lateinit var dialogLanguage: Dialog
    private lateinit var language: String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        linearLayout = inflater?.inflate(R.layout.fragment_drawer, container, false) as LinearLayout
        addControls();
        addEvents();
        return linearLayout
    }

    private fun addEvents() {
        linear_bookmark_click()
        linear_language_click()
    }

    private fun radio_english_change() {
        dialogLanguage.radio_english.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                language = "en"
            }
        }
    }

    private fun radio_vietnam_change() {
        dialogLanguage.radio_vietnam.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                language = "vn"
            }
        }
    }

    private fun linear_language_click() {
        linearLayout.linear_language.setOnClickListener {
            MainActivity.Instance.closeDrawer()
            showDialogLanguage()
        }
    }

    private fun showDialogLanguage() {
        dialogLanguage = Dialog(activity)
        dialogLanguage.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.setCancelable(true)
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.dialog_language)
        val window = dialogLanguage.getWindow()
        window!!.setGravity(Gravity.CENTER)
        dialogLanguage.show()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
        //////////////
        if (MethodStatic.getLanguage(requireContext()).equals("vn")) {
            dialogLanguage.radio_vietnam.isChecked = true
            dialogLanguage.radio_english.isChecked = false
        } else {
            dialogLanguage.radio_vietnam.isChecked = false
            dialogLanguage.radio_english.isChecked = true
        }
        radio_vietnam_change()
        radio_english_change()
        dialogLanguage.textview_cancel.setOnClickListener { dialogLanguage.dismiss() }
        dialogLanguage.textview_confirm.setOnClickListener {
            MethodStatic.setLanguage(requireContext(), language)
            changeLanguage(MethodStatic.getLanguage(requireContext()))
        }
    }

    private fun linear_bookmark_click() {
        linearLayout.linear_bookmark.setOnClickListener {
            var intent: Intent = Intent(activity, BookmarkActivity::class.java)
            startActivity(intent)
            Handler().postDelayed({
                MainActivity.Instance.closeDrawer()
            }, 1000)
        }
    }

    private fun addControls() {
        setupLayout()
    }

    private fun setupLayout() {
        Glide.with(this!!.activity!!).load(R.drawable.ic_bookmark_black).into(linearLayout.image_bookmark)
        Glide.with(this!!.activity!!).load(R.drawable.ic_global).into(linearLayout.image_language)
    }

    private fun changeLanguage(code: String?) {
        var myLocale: Locale = Locale(code)
        var res: Resources = resources
        var dm: DisplayMetrics = res.displayMetrics
        var conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        resetApp()
    }

    private fun resetApp() {
        var i: Intent? = activity?.baseContext?.packageManager
                ?.getLaunchIntentForPackage(activity?.baseContext?.packageName)
        i?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }
}

