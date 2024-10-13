package com.wspyo.ondootdo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wspyo.ondootdo.dao.TimeDao
import com.wspyo.ondootdo.entity.TimeEntity


@Database(entities = [TimeEntity::class], version = 5)
abstract class CommonDataBase : RoomDatabase(){

    abstract fun timeDao() : TimeDao

    companion object{
        @Volatile
        private var INSTANCE : CommonDataBase? = null

        fun getDatabase(
            context : Context
        ) : CommonDataBase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CommonDataBase::class.java,
                    "time_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

}