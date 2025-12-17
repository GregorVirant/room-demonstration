package com.test.roomtest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group")
data class Group(
    val name: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
