package com.example.locationtrackerapp.view.unSyncLocationFragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.locationtrackerapp.R
import com.example.locationtrackerapp.RoomDatabase.UserDetailsDao
import com.example.locationtrackerapp.RoomDatabase.UserLocationDatabase
import com.example.locationtrackerapp.baseClass.BaseFragment
import com.example.locationtrackerapp.databinding.FragmentUnSyncLocationsBinding
import com.example.locationtrackerapp.model.LocationInfo
import com.example.locationtrackerapp.model.UserLocationDetails
import com.example.locationtrackerapp.utils.unSyncLocationsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class unSyncLocationsFragment : BaseFragment() {

    private lateinit var binding:FragmentUnSyncLocationsBinding

    private val DATABASE_NAME :String = "USER_DATABASE"

    lateinit var userLocationDao: UserDetailsDao

    lateinit var listOfUserLocations:List<UserLocationDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUnSyncLocationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllLocation()

    }

    override fun callInitialServices(status: Boolean) {

        if(status){

            Toast.makeText(requireActivity(),
                getString(R.string.internet_is_connected), Toast.LENGTH_SHORT).show()
                if(listOfUserLocations.size > 0)
                    saveLocationsOnCloud()
        }
        else{
            Toast.makeText(requireActivity(),
                getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveLocationsOnCloud() {

        Toast.makeText(requireActivity(),
            getString(R.string.locations_are_syncing), Toast.LENGTH_SHORT).show()

        for(locationAtPosition in listOfUserLocations.indices){

            val locationInfo = LocationInfo(listOfUserLocations.get(locationAtPosition).latitude,listOfUserLocations.get(locationAtPosition).longtitude,listOfUserLocations.get(locationAtPosition).accurancy,listOfUserLocations.get(locationAtPosition).timeStamp,listOfUserLocations.get(locationAtPosition).speed)
            val dbRef = FirebaseDatabase.getInstance().getReference(listOfUserLocations.get(locationAtPosition).userName)

            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dbRef.setValue(locationInfo)

                     // Deleting 1 location details from specific location using userId
                     if(listOfUserLocations.size != 0)
                        userLocationDao.deleteLocationById(listOfUserLocations.get(locationAtPosition).userId)

                        if(listOfUserLocations.size - 1 == locationAtPosition){
                            Toast.makeText(requireActivity(), getString(R.string.all_locations_synced_successfully), Toast.LENGTH_SHORT).show()
                            getAllLocation()
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if(isVisibleToUser)
            getAllLocation()
    }

    private fun getAllLocation() {
        val db = Room.databaseBuilder(requireActivity(), UserLocationDatabase::class.java,DATABASE_NAME).allowMainThreadQueries().build()
        userLocationDao = db.userLocationDetails()

        listOfUserLocations  = userLocationDao.getAllLocationDetails()

        binding.unSyncLocationsRV.layoutManager = LinearLayoutManager(requireContext())
        binding.unSyncLocationsRV.adapter = unSyncLocationsAdapter(listOfUserLocations)

        if(listOfUserLocations.size == 0){
            binding.onDataFoundTxt.visibility = View.VISIBLE
            binding.unSyncLocationsRV.visibility = View.GONE
        }
        else{
            binding.onDataFoundTxt.visibility = View.GONE
            binding.unSyncLocationsRV.visibility = View.VISIBLE
        }
    }
}