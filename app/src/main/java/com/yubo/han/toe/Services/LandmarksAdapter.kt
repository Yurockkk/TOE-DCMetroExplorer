package com.yubo.han.toe.Services

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.yubo.han.toe.Constants

import com.yubo.han.toe.R
import com.yubo.han.toe.model.Landmarks

import kotlinx.android.synthetic.main.row_landmarks.view.*

/**
 * Created by han on 9/25/17.
 */
class LandmarksAdapter(private var context: Context,private var landmarkList: ArrayList<Landmarks>): RecyclerView.Adapter<LandmarksAdapter.ViewHolder>() {
    private val LOG_TAG = "LandmarksAdapter"

    lateinit var itemClickListener: OnItemClickListener


    override fun getItemCount() = landmarkList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_landmarks, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val landmark = landmarkList.get(position)
        holder.itemView.landmarkName.text = landmark.name
        holder.itemView.landmark_address.text = landmark.address
        holder.itemView.landmark_distance.text = "%.2f".format(landmark.distance * Constants.CENTIMETER_TO_MILE) + context.getString(R.string.distance_unit)

        // If no image, use default placeholder image
        Picasso.with(context).load(Uri.parse(landmark.imageString)).into(holder.itemView.landmarkImage)


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.landmarkHolder.setOnClickListener(this)
        }

        override fun onClick(view: View) = itemClickListener.onItemClick(itemView,
                landmarkList.get(adapterPosition))
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, landmarkData: Landmarks)
    }

    // setter method
    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}
