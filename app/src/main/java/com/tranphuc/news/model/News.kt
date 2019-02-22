package com.tranphuc.news.model

import io.realm.RealmObject

open class News : RealmObject {
    private var linkImage: String = ""
    private var title: String = ""
    private var time: String = ""
    private var linkHtml: String = ""
    private var type: Int = 0  // 0 -> item MainActivity, 1 -> item Bookmark, 2 -> Header News
    private var isBookMark : Boolean = false

    constructor()

    constructor(linkImage: String, title: String, time: String, linkHtml: String) {
        this.linkImage = linkImage
        this.title = title
        this.time = time
        this.linkHtml = linkHtml
    }

    public var LinkImage: String
        get() {
            return linkImage
        }
        set(value) {
            linkImage = value
        }

    public var Title: String
        get() {
            return title
        }
        set(value) {
            title = value
        }

    public var Time: String
        get() {
            return time
        }
        set(value) {
            time = value
        }

    public var LinkHtml: String
        get() {
            return linkHtml
        }
        set(value) {
            linkHtml = value
        }

    public var Type: Int
        get() {
            return type
        }
        set(value) {
            type = value
        }

    public var IsBookMark: Boolean
        get() {
            return isBookMark
        }
        set(value) {
            isBookMark = value
        }
}