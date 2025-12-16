package com.test.roomtest

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.test.roomtest.data.MyDatabase
import com.test.roomtest.data.Person
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // I recommend usage of singleton pattern for database instance because issues can happen
        // if multiple instances are opened simultaneously
        val db = Room.databaseBuilder(
            applicationContext,
            MyDatabase::class.java, "test-database"
        ).build()

        val personDao = db.personDao()


        lifecycleScope.launch { // Needed to call suspend functions
            personDao.upsert(Person("Ana", "Neki", 20))
            personDao.upsert(Person("Lolek", "Boldek", 20, "test@email.com"))

            personDao.getAll().forEach {
                Log.i("ROOM_TEST", it.toString())
            }

            personDao.getByName("Lolek").forEach {
                personDao.delete(it)
            }

            Log.i("ROOM_TEST", "After deletion of all Lolek:")

            personDao.getAll().forEach {
                Log.i("ROOM_TEST", it.toString())
            }

            Log.i("ROOM_TEST", "END")
        }
    }
}