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

    // 특정 ID의 알람 상태를 업데이트하는 메서드
    @Query("UPDATE time_table SET is_enabled = :isEnabled WHERE id = :id")
    fun updateAlarmStatus(id: Int, isEnabled: Boolean)

}