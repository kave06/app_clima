package com.example.kave.app_clima

import com.beust.klaxon.JsonObject
import com.beust.klaxon.double
import com.beust.klaxon.string

/**
 * Created by kave on 6/01/18.
 */

class PostCurrent {
    var currentTemp : Double? = null
    var currentHumi : Double? = null
    var currentDate : String? = null

    constructor(jsonObject: JsonObject){
        currentTemp = jsonObject.double("current_temp")
//        currentHumi = jsonObject.string("current_humi")
        currentHumi = jsonObject.double("current_humi")
        currentDate = jsonObject.string("current_datetime")
    }
}
