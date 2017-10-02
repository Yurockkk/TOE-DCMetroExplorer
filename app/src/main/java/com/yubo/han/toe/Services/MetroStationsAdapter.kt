package com.yubo.han.toe.Services

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.yubo.han.toe.R
import com.yubo.han.toe.model.MetroStations

import kotlinx.android.synthetic.main.row_metro_station.view.*
import com.squareup.picasso.Picasso
import com.yubo.han.toe.Utilities

/**
 * Created by han on 9/25/17.
 */
class MetroStationsAdapter(private var context: Context, private var stationList: ArrayList<MetroStations>): RecyclerView.Adapter<MetroStationsAdapter.ViewHolder>() {
    private val LOG_TAG = "MetroStationsAdapter"

    lateinit var itemClickListener: OnItemClickListener


    override fun getItemCount() = stationList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_metro_station, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Set all line button invisiable
        holder.itemView.metro_line_primary_button.visibility = View.INVISIBLE
        holder.itemView.metro_line_sec_button.visibility = View.INVISIBLE
        holder.itemView.metro_line_th_button.visibility = View.INVISIBLE

        val station = stationList.get(position)
        holder.itemView.stationName.text = station.name

        // Set primary metro line icon
        val primaryLineColor = Utilities.getLineColor(station.lineCode1)
        holder.itemView.metro_line_primary_button.setBackgroundResource(primaryLineColor)
        holder.itemView.metro_line_primary_button.visibility = View.VISIBLE

        // if more metrolines in this station
        if (!station.lineCode2.isNullOrEmpty()) {
            val secLineColor = Utilities.getLineColor(station.lineCode2.toString())
            holder.itemView.metro_line_sec_button.setBackgroundResource(secLineColor)
            holder.itemView.metro_line_sec_button.visibility = View.VISIBLE

            if (!station.lineCode3.isNullOrEmpty()) {
                val secLineColor = Utilities.getLineColor(station.lineCode3.toString())
                holder.itemView.metro_line_th_button.setBackgroundResource(secLineColor)
                holder.itemView.metro_line_th_button.visibility = View.VISIBLE
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.stationHolder.setOnClickListener(this)
        }

        override fun onClick(view: View) = itemClickListener.onItemClick(itemView,
                stationList.get(adapterPosition))
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, stationData: MetroStations)
    }

    // setter method
    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    // Search
    fun filterList(filterStations: ArrayList<MetroStations>) {
        this.stationList = filterStations
        notifyDataSetChanged()
    }
}