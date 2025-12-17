package com.test.roomtest.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "person",
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Person (
    val name: String,
    val surname: String? = null,
    val age: Int = 18,
    val email: String? = null,

    // Moved primary key to the bottom because it has a default value
    @PrimaryKey(autoGenerate = true) // by default false
    var id: Int = 0,
    val groupId: Int
)