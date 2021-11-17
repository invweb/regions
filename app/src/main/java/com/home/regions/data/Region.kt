package com.home.regions.data

import android.os.Parcel
import android.os.Parcelable
import androidx.navigation.Navigator

data class Region(
    val region: String,
    val city: String
) : Parcelable, Navigator.Extras {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(region)
        parcel.writeString(city)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Region> {
        override fun createFromParcel(parcel: Parcel): Region {
            val region : String = parcel.readString()!!
            val city : String = parcel.readString()!!
            return Region(region, city)
        }

        override fun newArray(size: Int): Array<Region?> {
            return arrayOfNulls(size)
        }
    }
}
