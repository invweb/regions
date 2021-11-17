package com.home.regions

import androidx.lifecycle.ViewModel
import java.io.InputStream
import java.nio.charset.Charset
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.home.regions.data.Region
import kotlinx.coroutines.CoroutineScope
import java.io.IOException

class MainViewModel : ViewModel() {
    fun loadRegions(context: Context): List<Region> {
        return try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.regions)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonOutput = String(buffer, Charset.defaultCharset())

            val regionsType = object : TypeToken<List<Region>>() {}.type
            Gson().fromJson(jsonOutput, regionsType)
        } catch (ex: IOException) {
            ex.printStackTrace()
            emptyList()
        }
    }

    fun obtainRegions(context: Context, coroutineScope: CoroutineScope): LiveData<List<Region?>> {
        val regionsLiveData : MutableLiveData<List<Region?>> = MutableLiveData<List<Region?>>()
        regionsLiveData.value = loadRegions(context)
        return regionsLiveData
    }
}
