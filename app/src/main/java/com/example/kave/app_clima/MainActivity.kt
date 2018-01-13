package com.example.kave.app_clima

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError

import kotlinx.android.synthetic.main.activity_main.*

import kotlin.concurrent.*

class MainActivity : AppCompatActivity() {

    private var temperature: Float = 0.toFloat()

    lateinit var recyclerView : RecyclerView
    lateinit var recycletViewAdapter : RecyclerView.Adapter<PostAdapterCurrent.ViewHolder>
    lateinit var recyclerViewLayoutManager : RecyclerView.LayoutManager
    var adapter: PostAdapterCurrent? = null
//    var adapter: PostAdapter? = null

//    var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var thermometer: Thermometer = findViewById(R.id.thermometer)

        setupViews()
        val timer = fixedRateTimer(period=60000.toLong(), daemon=true) {
            getData()
        }

        var btn : Button = findViewById(R.id.button_chart_temperature)
        btn.setOnClickListener{
            var intent : Intent = Intent(this,ChartTemperature::class.java)
            startActivity(intent)
        }

    }

    fun setupViews(){
        recyclerView = findViewById(R.id.recycler_view)
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
//        val posts = "https://jsonplaceholder.typicode.com/posts"
        val posts = "http://157.88.58.134:5578/current"
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

            var c3: CircularProgressBar = findViewById(R.id.circularprogressbar1)
            var hum =  postCurrent.first().currentHumi!!
            var humINT: Int? = hum.toInt()
            c3.setTitle("$hum %")
            c3.setSubTitle("Humedad")
            humINT?.let { c3.setProgress(it) }

            var temp2: Double? = postCurrent.first().currentTemp
            temperature = temp2!!.toFloat()
            thermometer.setCurrentTemp(temperature)

            var tempString: String = temp2.toString()
            tempView.setText(tempString+" ºC")





        },failure ={ error ->

        } )
    }

}

private fun TextView.setText(tempString: String, function: () -> Unit) {}
