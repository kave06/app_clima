package com.example.kave.app_clima

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
//import android.support.v4.widget.SwipeRefreshLayout

import kotlin.concurrent.*

class ChartTemperature : AppCompatActivity() {

    private var temperature: Float = 0.toFloat() //inicializo temperaura como float

    lateinit var recyclerView : RecyclerView
    lateinit var recycletViewAdapter : RecyclerView.Adapter<PostAdapterCurrent.ViewHolder>
    lateinit var recyclerViewLayoutManager : RecyclerView.LayoutManager
    var adapter: PostAdapterCurrent? = null
//    var adapter: PostAdapter? = null

//    var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_temperature)

//        mSwipeRefreshLayout = findViewById(R.id.srlContainer) as SwipeRefreshLayout

//        var c3: CircularProgressBar = findViewById(R.id.circularprogressbar1)

        setupViews()
        val timer = fixedRateTimer(period=60000.toLong(), daemon=true) {
            getData()
        }

    }

    fun setupViews(){
        recyclerView = findViewById(R.id.recycler_view_chart_temperature)
        recyclerViewLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = recyclerViewLayoutManager
    }

    fun getRequest(url: String, success: (String) -> Unit, failure: (FuelError) -> Unit) {

        Fuel.get(url).responseString() { request, response, result ->

            val (data, error) = result
            if (error != null) {
                Log.v("Error", error!!.toString())
                failure(error)
            } else {
                val onSuccess = data ?: return@responseString
                success(onSuccess)

            }

        }
    }

    fun getData(){
        val posts = "http://157.88.58.134:5578/ambient/days/1"
        getRequest(posts,success = { response ->

            val parser = Parser()
            val stringBuilder = StringBuilder(response)
            val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
//            val postModel  = model.map { PostModel(it)}.filterNotNull()
            val postCurrent  = model.map { PostCurrent(it)}.filterNotNull()
            println("-------------------------------")
            println("postCurrent: " + postCurrent)

            this.adapter = PostAdapterCurrent(postCurrent)
            recycletViewAdapter = adapter!!
            recyclerView.adapter = recycletViewAdapter
            recycletViewAdapter.notifyDataSetChanged()
            adapter?.onClick = { view ->

                val itemPosition = recyclerView.getChildLayoutPosition(view)
                Log.d("ITEM:POSITION",postCurrent[itemPosition].currentDate)
            }

            Log.d("code", postCurrent.first().currentTemp.toString())

            var temp2: Double? = postCurrent.first().currentTemp
            temperature = temp2!!.toFloat()

        },failure ={ error ->

        } )
    }

}

//private fun TextView.setText(tempString: String, function: () -> Unit) {}
