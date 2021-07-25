package com.souvik.todayindia.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg data: DataSet)

    @Query("Select * from tbl_weather order by id")
    fun getAll(): List<DataSet>

    @Query("Delete from tbl_weather")
   fun removeAll()

    @Query("Select count(1) from tbl_weather")
    fun getRecordCount() : Int
}