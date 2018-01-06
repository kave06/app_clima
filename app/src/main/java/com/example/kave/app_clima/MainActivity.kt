package com.example.kave.app_clima

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

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView
    lateinit var recycletViewAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder>
    lateinit var recyclerViewLayoutManager : RecyclerView.LayoutManager
    var adapter: PostAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        getData()

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
        val posts = "https://jsonplaceholder.typicode.com/posts"
        getRequest(posts,success = { response ->

            val parser = Parser()
            val stringBuilder = StringBuilder(response)
            val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
            val postModel  = model.map { PostModel(it)}.filterNotNull()

            this.adapter = PostAdapter(postModel)
            recycletViewAdapter = adapter!!
            recyclerView.adapter = recycletViewAdapter
            recycletViewAdapter.notifyDataSetChanged()
            adapter?.onClick = { view ->

                val itemPosition = recyclerView.getChildLayoutPosition(view)
                Log.d("ITEM:POSITION",postModel[itemPosition].body)
            }

            Log.d("code",postModel.first().body)
            Log.d("Mapped::",postModel.first().title)
            Log.d("dode",postModel.first().userId.toString())
            Log.d("Mapped::",postModel.first().id.toString())

        },failure ={ error ->

        } )
    }

}
