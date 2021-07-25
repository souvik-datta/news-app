package com.souvik.todayindia

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.souvik.todayindia.local.AppDatabase
import com.souvik.todayindia.local.DataSet
import com.souvik.todayindia.model.WeatherData
import com.souvik.todayindia.network.ApiClient
import com.souvik.todayindia.utility.BASE_URL_WEATHER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(val context: Application) : AndroidViewModel(context) {
    var network: MutableLiveData<Boolean> = MutableLiveData()
    val _response = MutableLiveData<WeatherData>()
    val response: LiveData<WeatherData>
        get() = _response

    fun isNetworkAvailable(context: Context?): LiveData<Boolean> {
        network = MutableLiveData()
        if (context == null) {
            network.value = false
            return network
        }
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        network.value = true
                        return network
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        network.value = true
                        return network
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        network.value = true
                        return network
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                network.value = true
                return network
            }
        }
        network.value = false
        return network
    }

    fun getWeatherInfo(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ApiClient.destroyRetrofit()
                ApiClient.changeBaseUrl(BASE_URL_WEATHER)
                val result = ApiClient.apiInterface?.getCurrentWeatherData(
                    lat = latitude.toString(),
                    lon = longitude.toString()
                )
                if (result?.isSuccessful == true) {
                    _response.postValue(result.body())
                    val c: Date = Calendar.getInstance().getTime()
                    val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val formattedDate: String = df.format(c)
                    AppDatabase.getDatabase(context)?.insertAll(
                        DataSet(
                            latitude = result.body()?.coord?.lat ?: 0.0,
                            longitude = result.body()?.coord?.lon ?: 0.0,
                            temp = result.body()?.main?.temp.toString(),
                            feelTemp = result.body()?.main?.feels_like.toString(),
                            date = formattedDate
                        )
                    )
                }
            } catch (e: Exception) {
                Log.d("TAG", "getWeatherInfo: ${e.localizedMessage}")
            }
        }
    }

}