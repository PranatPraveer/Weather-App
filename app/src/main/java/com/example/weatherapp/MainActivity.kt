package com.example.weatherapp

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    //weather url to get JSON
    var weather_url1 = ""
    //api id for url
    var api_id1 = "f7c480a28a3a4141b83ef6e1d2c023b9"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.e("lat", weather_url1)
        //on clicking this button function to get the coordinates will be called
            Log.e("lat", "onClick")
            //function to find the coordinates of the last location
        //checkLocationPermission(1)
        obtainLocation()
        info.setOnClickListener{
            val url:String="https://github.com/PranatPraveer"
            val intent= Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
            true
        }
    }
    @TargetApi(27)
    private fun checkLocationPermission(locationRequestCode:Int){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permlist= arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissions(permlist,locationRequestCode)
            obtainLocation()
        }
return    }
   // @SuppressLint("MissingPermission")
    private fun obtainLocation(){
       checkLocationPermission(1)
        Log.e("lat", "function")
        //get the last location
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    //get the latitute and longitude and create the http URL
                    weather_url1 = "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude +"&lon="+ location?.longitude + "&key="+ api_id1
                    Log.e("lat", weather_url1.toString())
                    //this function will fetch data from URL
                    sunrise.text=location?.latitude.toString()
                    sunset.text=location?.longitude.toString()
                    getTemp()
                }
    }

    fun getTemp() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = weather_url1
        Log.e("lat", url)
        // Request a string response from the provided URL.
        val stringReq = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.e("lat", response.toString())
                //get the JSON object
                val obj = JSONObject(response)
                //get the Array from obj of name - "data"
                val arr = obj.getJSONArray("data")
                Log.e("lat obj1", arr.toString())
                //get the JSON object from the array at index position 0
                val obj2 = arr.getJSONObject(0)
                Log.e("lat obj2", obj2.toString())
                val obj6=arr.getJSONObject(0)


                temp.text = obj2.getString("temp")+"°C"
                address.text=obj2.getString("city_name")

                pressure.text=obj6.getString("pres")
                wind.text=obj6.getString("wind_spd")
                humidity.text=obj6.getString("aqi")
                Visibility.text="visibility - "+ obj6.getString("vis")

            },
            //In case of any error
            Response.ErrorListener { Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show() })
        queue.add(stringReq)
    }
}