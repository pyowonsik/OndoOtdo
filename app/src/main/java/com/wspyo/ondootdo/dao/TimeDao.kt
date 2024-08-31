package com.wspyo.ondootdo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wspyo.ondootdo.entity.TimeEntity

@Dao
interface TimeDao  {
    @Query("SELECT * FROM time_table")
    fun getAllTimes() : List<TimeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTime(text : TimeEntity)

    @Query("DELETE FROM time_table")
    fun deleteAllTimes()

}