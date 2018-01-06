package com.example.kave.app_clima

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

/**
 * Created by kave on 6/01/18.
 */

class PostAdapterCurrentTemp(dataSet: List<PostCurrentTemp>) : RecyclerView.Adapter<PostAdapterCurrentTemp.ViewHolder>(){

    val dataSet : List<PostCurrentTemp> = dataSet
    final var onClick : (View)-> Unit = {}

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val titleTextView = view.findViewById<TextView>(R.id.title_text_view) as TextView
        val bodyTextView = view.findViewById<TextView>(R.id.body_text_view) as TextView
//        var bodyFloatView = view.findViewById<EditText>(R.id.current_temp) as EditText

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.current_temp, parent,false)

        view.setOnClickListener(View.OnClickListener { view ->
            this.onClick(view)
        })
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.let { holder ->
//            holder.titleTextView.text = this.dataSet[position].currentTemp
//            holder.bodyFloatView.editableText = this.dataSet[position].currentTemp
//            holder.bodyTextView.text = this.dataSet[position].body
//            holder.bodyFloatView.text = this.dataSet[position].currentTemp
//            holder.bodyFloatView.Double = this.dataSet[position].currentTemp
//            holder.bodyFloatView.text = this.dataSet[position].currentTemp
        }

    }


    override fun getItemCount(): Int {

        return  this.dataSet.size
    }

}