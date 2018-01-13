package com.example.kave.app_clima

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.mikephil.charting.charts.LineChart
import kotlin.concurrent.*

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class ChartTemperature : AppCompatActivity() {

    lateinit var lineChart : LineChart



    private var temperature: Float = 0.toFloat() //inicializo temperaura como float

    lateinit var recyclerView : RecyclerView
    lateinit var recycletViewAdapter : RecyclerView.Adapter<PostAdapterCurrent.ViewHolder>
    lateinit var recyclerViewLayoutManager : RecyclerView.LayoutManager
    var adapter: PostAdapterCurrent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_temperature)

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


            lineChart = findViewById(R.id.lineChart)


            var yAXES : ArrayList<Entry>? = null
            var xAXES : ArrayList<Entry>? = null


            yAXES?.add(Entry(15f, 1f))
            yAXES?.add(Entry(16f, 2f))
            yAXES?.add(Entry(17f, 3f))
            yAXES?.add(Entry(18f, 4f))
            yAXES?.add(Entry(19f, 5f))
            yAXES?.add(Entry(20f, 6f))
            xAXES?.add(Entry(1f,1f))
            xAXES?.add(Entry(2f,2f))
            xAXES?.add(Entry(3f,3f))
            xAXES?.add(Entry(4f,4f))
            xAXES?.add(Entry(5f,5f))
            xAXES?.add(Entry(6f,6f))



            var lineDataSets : ArrayList<ILineDataSet>? = null
            var lineDataSet1 : LineDataSet? = LineDataSet(yAXES, "Temperature")

            lineDataSet1?.setDrawCircles(false)
            lineDataSet1?.setColor(Color.BLUE)

            lineDataSets?.add(lineDataSet1!!)

            val lineData = LineData(lineDataSet1)
            lineChart.data = lineData

        },failure ={ error ->

        } )
    }

}