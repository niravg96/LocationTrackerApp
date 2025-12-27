package com.example.locationtrackerapp.RoomDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.locationtrackerapp.model.UserLocationDetails

@Dao
interface UserDetailsDao {

    @Insert
    fun insertLocations(vararg userLocationDetails: UserLocationDetails)

    @Query("SELECT * FROM userlocationdetails")
    fun getAllLocationDetails() : List<UserLocationDetails>

    @Query("DELETE FROM userlocationdetails WHERE userId = :userNameId")
    fun deleteLocationById(userNameId:Int)

}