package com.example.locationtrackerapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserLocationDetails(

    @ColumnInfo(name = "userName") val userName:String,
    @ColumnInfo(name = "latitude") val latitude:Double,
    @ColumnInfo(name = "longtitude") val longtitude:Double,
    @ColumnInfo(name = "accurancy") val accurancy:Float,
    @ColumnInfo(name = "timeStamp") val timeStamp:Long,
    @ColumnInfo(name = "speed") val speed:Float
){
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0
}
