package com.souvik.todayindia.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tbl_weather")
data class DataSet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val temp: String = "",
    @ColumnInfo(name = "feel_temp") val feelTemp: String = "",
    val date: String = ""
) : Serializable