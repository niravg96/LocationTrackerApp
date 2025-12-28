package com.example.locationtrackerapp.view.userLocationActivity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.locationtrackerapp.R
import com.example.locationtrackerapp.baseClass.baseActivity
import com.example.locationtrackerapp.databinding.ActivityUserLocationBinding
import com.example.locationtrackerapp.utils.ViewPagerAdapter
import com.example.locationtrackerapp.view.submitLocationFragment.submitLocationFragment
import com.example.locationtrackerapp.view.unSyncLocationFragment.unSyncLocationsFragment


class UserLocationActivity : baseActivity()  , View.OnClickListener  {

    private lateinit var binding:ActivityUserLocationBinding

    private var viewPagerAdapter: ViewPagerAdapter? = null

    override fun callInitialServices(status: Boolean) {

        if(status){
            Toast.makeText(applicationContext,"Internet is connected", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(applicationContext,"No internet connection",Toast.LENGTH_SHORT).show()
        }
    }
    override fun callObservers() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserLocationBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.back.setOnClickListener(this)

        // setting up the adapter
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        // add the fragments
        viewPagerAdapter!!.add(submitLocationFragment(), getString(R.string.user_logs))

        viewPagerAdapter!!.add(unSyncLocationsFragment(), getString(R.string.unsync_logs))

        // Set the adapter
        binding.viewpager.adapter = viewPagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }
    override fun onClick(p0: View?) {
        when(p0!!.id){

            R.id.back -> {
                finish()
            }
        }
    }
}