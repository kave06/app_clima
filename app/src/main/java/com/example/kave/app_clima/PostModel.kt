package com.example.kave.app_clima

import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string

/**
 * Created by kave on 6/01/18.
 */

class PostModel {

    var id : Int? = null
    var userId : Int? = null
    var body : String? = null
    var title : String? = null

    constructor(jsonObject: JsonObject) {
        id = jsonObject.int("id")
        userId = jsonObject.int("userId")
        body = jsonObject.string("body")
        title = jsonObject.string("title")
    }
}
