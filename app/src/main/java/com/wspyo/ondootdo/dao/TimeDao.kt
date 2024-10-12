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

    @Query("DELETE FROM time_table WHERE id = :id")
    fun deleteTime(id : Int)

    @Query("UPDATE time_table SET time = :time WHERE id = :id")
    fun updateTime(id : Int , time : String)

    @Query("UPDATE time_table SET is_enabled = :isEnabled WHERE id = :id")
    fun updateAlarmStatus(id: Int, isEnabled: Boolean)

}