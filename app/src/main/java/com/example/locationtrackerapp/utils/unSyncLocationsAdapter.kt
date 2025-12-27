package com.example.locationtrackerapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtrackerapp.databinding.RowUnsyncLocationsBinding
import com.example.locationtrackerapp.model.UserLocationDetails

class unSyncLocationsAdapter(var userLocations:List<UserLocationDetails>) : RecyclerView.Adapter<unSyncLocationsAdapter.ViewHolder>() {

    class ViewHolder(binding: RowUnsyncLocationsBinding) : RecyclerView.ViewHolder(binding.root){

        val userName = binding.userNameTxt
        val latitude = binding.latitudeTxt
        val longtitude = binding.longtitudeTxt
        val accuracy = binding.AccuracyTxt
        val time = binding.TimeTxt
        val speed = binding.speedTxt
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = RowUnsyncLocationsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userLocations = userLocations[position]

        holder.userName.setText("User name : "+userLocations.userName)
        holder.latitude.setText("Latitude : "+userLocations.latitude.toString())
        holder.longtitude.setText("Longtitude : "+userLocations.longtitude.toString())
        holder.accuracy.setText("Accrancy : "+userLocations.accurancy.toString())
        holder.time.setText("TimeStamp : "+userLocations.timeStamp.toString())
        holder.speed.setText("Speed : "+userLocations.speed.toString())
    }

    override fun getItemCount(): Int {
        return userLocations.size
    }


}