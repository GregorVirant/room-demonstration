package com.test.roomtest.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PersonDao {
    /*
    This would work well but i prefer Upsert
    @Insert
    suspend fun insert(person: Person) : Long

    @Update
    suspend fun update(person: Person)
    */

    // Inserts a new person or updates an existing one depending on the primary key
    // If id = 0 (with autoGenerate), inserts and returns new row id.
    // If id matches existing, updates and returns -1.
    @Upsert
    suspend fun upsert(person: Person) : Long //Functions can also be marked as suspend

    @Delete
    suspend fun delete(person: Person)

    @Query("SELECT * FROM person")
    suspend fun getAll() : List<Person> // As mentioned before use of Flow is also supported here

    @Query("SELECT * FROM person WHERE id = :id")
    suspend fun getById(id: Int) : Person?

    @Query("SELECT * FROM person WHERE name = :name")
    suspend fun getByName(name: String) : List<Person>

    @Query("DELETE FROM person WHERE id = :id")
    suspend fun deleteById(id: Int)
}