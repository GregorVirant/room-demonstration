package com.test.roomtest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Person::class, Group::class], version = 2)
abstract class MyDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun groupDao(): GroupDao
}

// The following migration is mostly genereted using copilot, since it is only used for testing fk
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `group` (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS person_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                surname TEXT,
                age INTEGER NOT NULL,
                email TEXT,
                groupId INTEGER NOT NULL,
                FOREIGN KEY(groupId) REFERENCES `group`(id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO person_new (id, name, surname, age, email, groupId)
            SELECT id, name, surname, age, email, 1 FROM person
        """.trimIndent())

        db.execSQL("DROP TABLE person")

        db.execSQL("ALTER TABLE person_new RENAME TO person")
    }
}
