package com.souvik.todayindia

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.souvik.todayindia.model.NewsData
import com.souvik.todayindia.network.ApiClient
import com.souvik.todayindia.utility.BASE_URL_NEWS
import com.souvik.todayindia.utility.apiKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(private val context: Application) : AndroidViewModel(context) {

    var _list = MutableLiveData<ArrayList<NewsData>>()
    val list : LiveData<ArrayList<NewsData>>
        get() = _list

    fun getHeadLineNews(category:String?=null){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                ApiClient.destroyRetrofit()
                ApiClient.changeBaseUrl(BASE_URL_NEWS)
                val result = ApiClient.apiInterface?.getTopHeadlines(country = "in", apiKey = apiKey,category = category)
                if(result?.isSuccessful==true){
                    _list.postValue(result.body()?.articles as ArrayList<NewsData>)
                }
            }catch (e:Exception){

            }
        }
    }
}