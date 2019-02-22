package com.tranphuc.news.model

class Category {
    private var nameCategory: String = ""
    private var isActive: Boolean = false

    constructor(nameCategory: String, isActive: Boolean) {
        this.nameCategory = nameCategory
        this.isActive = isActive
    }

    constructor()


    public var NameCategory: String
        get() {
            return nameCategory
        }
        set(value) {
            nameCategory = value
        }

    public var IsActive: Boolean
        get() {
            return isActive
        }
        set(value) {
            isActive = value
        }
}