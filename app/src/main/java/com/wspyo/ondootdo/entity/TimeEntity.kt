package com.wspyo.ondootdo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// room의 반환 값은 Entity 로 매핑
@Entity("time_table")
data class TimeEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int,
    @ColumnInfo(name = "word")
    var time : String,
    // alarm on/off
    var alarm : Boolean,
)