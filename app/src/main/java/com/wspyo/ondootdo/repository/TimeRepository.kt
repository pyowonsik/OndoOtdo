package com.wspyo.ondootdo.repository

import android.content.Context
import com.wspyo.ondootdo.db.CommonDataBase
import com.wspyo.ondootdo.entity.TimeEntity

class TimeRepository(context : Context) {

    val db = CommonDataBase.getDatabase(context)

    fun getAllTimes() = db.timeDao().getAllTimes()
    fun insertTime(time : String) = db.timeDao().insertTime(TimeEntity(0,time,true))

    fun deleteAllTimes() = db.timeDao().deleteAllTimes()

    fun deleteTime(id : Int) = db.timeDao().deleteTime(id)

    fun updateTime(id : Int , time: String) = db.timeDao().updateTime(id,time)

    fun updateAlarmStatus(id:Int , isEnabled : Boolean) = db.timeDao().updateAlarmStatus(id,isEnabled)
}