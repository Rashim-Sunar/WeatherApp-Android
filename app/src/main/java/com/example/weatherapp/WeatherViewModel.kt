package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.WeatherModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel(){

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableStateFlow<NetworkResponse<WeatherModel>>(
        value = NetworkResponse.Loading
    )
    val weatherResult : Flow<NetworkResponse<WeatherModel>> get() = _weatherResult


    fun getData(city: String){
       viewModelScope.launch {
           try{

               _weatherResult.value = NetworkResponse.Loading

               val response = weatherApi.getWeather(apiKey = Constant.apiKey, city = city)
               if(response.isSuccessful){
                   //Log.d("Response", response.body().toString())
                   response.body()?.let{
                       _weatherResult.value = NetworkResponse.Success(it)
                   }
               }else{
                   //Log.d("Error", response.message())
                   _weatherResult.value = NetworkResponse.Error("Failed to load data")
               }
           }catch(e : Exception){
               _weatherResult.value = NetworkResponse.Error("Failed to load data")
           }
       }
    }
}