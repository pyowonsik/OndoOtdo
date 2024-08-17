package com.wspyo.ondootdo.repository

import android.content.Context
import com.wspyo.ondootdo.db.CommonDataBase
import com.wspyo.ondootdo.entity.TimeEntity

class TimeRepository(context : Context) {

    val db = CommonDataBase.getDatabase(context)

    fun getAllTimes() = db.timeDao().getAllTimes()
    fun insertTime(time : String) = db.timeDao().insertTime(TimeEntity(0,time))
}