package com.example.locationtrackerapp.view.submitLocationFragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.example.locationtrackerapp.R
import com.example.locationtrackerapp.RoomDatabase.UserLocationDatabase
import com.example.locationtrackerapp.baseClass.BaseFragment
import com.example.locationtrackerapp.databinding.FragmentSubmitLocationBinding
import com.example.locationtrackerapp.model.LocationInfo
import com.example.locationtrackerapp.model.UserLocationDetails
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class submitLocationFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding:FragmentSubmitLocationBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val LOCATION_PERMISSION_REQUEST = 100

    private val DATABASE_NAME :String = "USER_DATABASE"

    var lat : Double = 0.0
    var lon : Double = 0.0
    var accuracy : Float = 0.0F
    var timeStamp : Long = 2122322
    var speed : Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubmitLocationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.getLocationBtn.setOnClickListener(this)

        binding.saveLocationBtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {

        when(p0!!.id){

            R.id.getLocationBtn ->{
                getCurrentLocation()
            }
            R.id.saveLocationBtn ->{

                if(binding.fetchLocationTxt.text.equals("--"))
                    Toast.makeText(requireActivity(),
                        getString(R.string.please_get_your_location),Toast.LENGTH_SHORT).show()

                else if(binding.userNameEdt.text.toString().trim().isNullOrEmpty())
                    Toast.makeText(requireActivity(),
                        getString(R.string.please_enter_user_name),Toast.LENGTH_SHORT).show()

                else if(!isNetworkAvailable(requireActivity()))
                    insertLocationDetailsLocally()

                else{

                    binding.progressBar.visibility = View.VISIBLE
                    var userName = binding.userNameEdt.text.toString().trim()

                    val locationInfo = LocationInfo(lat,lon,accuracy,timeStamp,speed)

                    val dbRef = FirebaseDatabase.getInstance().getReference(userName)

                    dbRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            dbRef.setValue(locationInfo)
                            Toast.makeText(requireActivity(),
                                getString(R.string.data_saved_successfully),Toast.LENGTH_SHORT).show()
                            binding.userNameEdt.setText("")

                            binding.fetchLocationTxt.setText("--")

                            binding.progressBar.visibility = View.GONE

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(requireActivity(),getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show()

                            binding.progressBar.visibility = View.GONE
                        }
                    })
                }
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                    accuracy  = location.accuracy
                    timeStamp = location.time
                    speed  = location.speed

                    binding.fetchLocationTxt.text = "latitude : "+lat+"\n"+"Longtitude : "+lon+"\n"+ "Accuracy : "+accuracy +"\n"+ "Time : "+timeStamp +"\n"+"Speed : "+speed

                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Location not available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun callInitialServices(status: Boolean) {


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        }
    }

    private fun insertLocationDetailsLocally() {

        val db = Room.databaseBuilder(requireActivity(), UserLocationDatabase::class.java,DATABASE_NAME).allowMainThreadQueries().build()

        val userLocationDao = db.userLocationDetails()

        userLocationDao.insertLocations(UserLocationDetails(binding.userNameEdt.text.toString().trim(),lat,lon,accuracy,timeStamp,speed))

        Toast.makeText(requireActivity(),
            getString(R.string.data_stored_successfully),Toast.LENGTH_SHORT).show()
        binding.userNameEdt.setText("")

        binding.fetchLocationTxt.setText("--")
    }
    fun isNetworkAvailable(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}