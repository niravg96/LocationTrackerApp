package com.example.locationtrackerapp.RoomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.locationtrackerapp.model.UserLocationDetails

@Database(entities = arrayOf(UserLocationDetails::class), version = 1)
abstract class UserLocationDatabase : RoomDatabase() {

    abstract fun userLocationDetails() : UserDetailsDao

}