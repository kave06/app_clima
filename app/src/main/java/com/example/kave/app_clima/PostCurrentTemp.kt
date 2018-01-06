package com.example.kave.app_clima

import com.beust.klaxon.JsonObject
import com.beust.klaxon.double
import com.beust.klaxon.int
import com.beust.klaxon.string

/**
 * Created by kave on 6/01/18.
 */

class PostCurrentTemp {
    var currentTemp : String? = null

    constructor(jsonObject: JsonObject){
        currentTemp = jsonObject.string("currentTemp")
    }
}
