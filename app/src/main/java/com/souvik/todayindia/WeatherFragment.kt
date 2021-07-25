package com.souvik.todayindia

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.souvik.todayindia.databinding.FragmentWeatherBinding
import java.lang.StringBuilder
import java.util.*

class WeatherFragment : Fragment() {
    private val REQUEST_CODE = 200
    private var flag: Boolean = true
    private var userLocation: Location = Location("")
    private var locationData: Location = Location("")
    private lateinit var addresses: List<Address>
    private val viewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }
    private lateinit var binding: FragmentWeatherBinding
    private val LOCATION_REQUEST: Int = 20
    private lateinit var mLocationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_weather, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionCheck()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val stringBuilder = StringBuilder()
            stringBuilder.apply {
                append("Coordinates: \n")
                append("Latitude: ${it.coord.lat} Longitude: ${it.coord.lon}\n")
                append("Main: ${it.weather[0].main}\nDescription: ${it.weather[0].description}\n")
                append("Base: ${it.base}\n")
                append("Temp: ${(it.main.temp - 273.15).toInt()}\nFeels like: ${it.main.feels_like - 273.15}\n")
            }
            binding.location.text = stringBuilder.toString()
        })
    }

    private fun getRuntimePermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_CODE
            )
            return false
        } else
            return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            for (i in grantResults.indices) {
                if (grantResults[i] == -1) {
                    flag = !flag
                    Log.d("TAG", "All permissions are not provided")
                    Handler().postDelayed({
                        getRuntimePermissions()
                    }, 2000)
                    break
                }
            }
            if (flag) {
                viewModel.isNetworkAvailable(requireContext())
                    .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        if (!it) {
                            Snackbar.make(
                                binding.root,
                                "Please allow all the permissions",
                                Snackbar.LENGTH_LONG
                            ).show()

                        } else {
                            Snackbar.make(
                                binding.root,
                                "Please turn on the internet",
                                Snackbar.LENGTH_LONG
                            ).show()

                        }
                    })
            }
        }else if(requestCode == LOCATION_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            fetchLocation()
        }
    }


    private fun getAddress(userLocation: Location) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        Log.d("TAG", "Gocoder present: " + Geocoder.isPresent())
        try {
            addresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1)
            Log.d("TAG", "Address details: $addresses[0].subAdminArea")
            binding.location.text = addresses[0].subAdminArea
        } catch (e: Exception) {
            Log.d("TAG", "exception: ${e.message}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.isCostAllowed = true
        criteria.accuracy = Criteria.ACCURACY_FINE
        val bestProvider: String? = locationManager.getBestProvider(criteria, true)
        try {
            locationData = bestProvider?.let { locationManager.getLastKnownLocation(it) }!!
            val lat: Double = locationData.longitude
            val lon: Double = locationData.longitude
            Log.d("TAG", "Latitude and longitude: $lat and $lon")
            userLocation = locationData
            getAddress(userLocation)
        } catch (e: java.lang.Exception) {
            val lat: Double = -1.0
            val lon: Double = -1.0
            Log.d("TAG", "Latitude and Longitude: $lat and $lon and exception: $e.message")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun permissionCheck() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // You can use the API that requires the permission.
            Log.d("TAG", "Permission granted..")
            fetchLocation()

        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST
            )
        }
    }

    //Fetching current user location.
    fun fetchLocation() {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.apply {
            this.interval = 60000
            this.fastestInterval = 5000
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d(
                    "TAG",
                    "Latitude: ${locationResult.lastLocation.latitude} and Longitude: ${locationResult.lastLocation.longitude}"
                )
                viewModel.getWeatherInfo(locationResult.lastLocation.latitude,locationResult.lastLocation.longitude)
                LocationServices.getFusedLocationProviderClient(requireActivity())
                    .removeLocationUpdates(mLocationCallback)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //In case location permission is not given.
            return
        }
        LocationServices.getFusedLocationProviderClient(requireActivity())
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }
}