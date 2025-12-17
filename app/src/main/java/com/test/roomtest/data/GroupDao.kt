package com.test.roomtest.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GroupDao {
    @Insert
    suspend fun insert(group: Group): Long

    @Delete
    suspend fun delete(group: Group)

    @Query("SELECT * FROM `group`")
    suspend fun getAll(): List<Group>
}