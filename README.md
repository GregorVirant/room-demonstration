# Room demonstracija
[Room](https://developer.android.com/training/data-storage/room)
## Opis in ostale informacije
- Za lokalno shranjevanje podatkov v SQLbazi
- Najbolj pogost primer uporabe: cachiranje poemembnih podatkov, da bodo dostopni tudi ko aplikcaija nima dostopa do interneta


Del android jetpack in androidx.

Opisnik
Mogoc tut dovoljenja

Je kompatibilno tudi z kotlin multiplatform.
Podpora LiveData
Podpora asinhronosti z korutinami in tudi RXJava

### Razlog izbure
### Prednosti
- Abstrakcija SQLite z lažjo uporabo
- Še vedno lahko uporablja celotno moč SQLite
- Compile-time verifikacija SQL qurijev
- S pomočjo anotacij se izgnemo veliko "boilerplate" s tem tudi zmansamo možnosti napak
- Poenostavljene [migracije](https://developer.android.com/training/data-storage/room/migrating-db-versions#test) (avtomatske in ročne)
- Izvoz shem v JSON datotekah (Omogoča hranjenje zgodovin shem in posredno testiranje prejšnjih stanj baze)
- [Developer Android](https://developer.android.com/training/data-storage/sqlite) priporoča uporabo Room kot abstrakcijski sloj za pridobivanje podatkov iz SQLite podatkovnih baz naše aplikacije.
### Slabosti
### Licenca
Apache 2.0
https://www.apache.org/licenses/LICENSE-2.0
### Število uporabnikov
Na youtubu imajo vedeji o roomu okoli 200 tisoč ogledov.
### Časovna in prostorska zahtevnost
### Vzdrževanje tehnologije
- Zadnja sprememba: November 19, 2025, stable release 2.8.4
- Letos izdanih 9 relesov (2.7.0, 2.7.1, 2.7.3, 2.8.0, 2.8.1, 2.8.2, 2.8.2, 2.8.3, 2.8.4) in še nekaj alf in bet
- Imajo še 244 aktivh prijavljenih [hroščov/zahtevkov za dodatke](https://issuetracker.google.com/issues?q=componentid:413107%20status:open)
### Dovoljenja
- Ne potrebuje nobenih posebnih dovoljenj, razen če poganjamo DB operacije na glavni niti

## Setup
- [Preberi več](https://developer.android.com/training/data-storage/room#setup)
- Moj primer podpira samo kotlin z `build.gradle.kts`
- V `build.gradle.kts` od `app`
```Gradle
dependencies {
    val room_version = "2.8.4" // My version (current latest)
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version") // OPTIONAL For kotlin coroutines
    ksp("androidx.room:room-compiler:$room_version") // Kotlin Symbol Processing (annotation processor)
}
```
- Potreben je tudi [ksp](https://developer.android.com/build/migrate-to-ksp#add-ksp)

## Primer Uporabe
- Stvari, ki v tem primeru nebodo omnjene [Migracije](https://developer.android.com/training/data-storage/room/migrating-db-versions), [Tuji ključi](https://medium.com/@vontonnie/connecting-room-tables-using-foreign-keys-c19450361603) in [Converters](https://developer.android.com/training/data-storage/room/referencing-data) (shranjvanje kompleknih tipov, recimo date time)
- Recimo da pripravimo bazo z tableo Person, ki vsebuje ljudi, potrebujemo:
    - Person (class and entity)
    - PersonDao (Dao interface)
    - MyDatabase (Room database)
<img width="339" height="99" alt="image" src="https://github.com/user-attachments/assets/ea744493-17e1-4597-a4ed-68fb92fea3fe" />

### Person
- Pripravimo razred Person, ki ga anotiramo kot entity
- Določimo tudi primary key, v mojem primeru torej id
- Jaz sem tudi določil da se naj id avtomatsko generira z `autoGenerate = true`.
```Kotlin
package com.test.roomtest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person")
data class Person (
    val name: String,
    val surname: String? = null,
    val age: Int = 18,
    val email: String? = null,

    // Moved primary key to the bottom because it has a default value
    @PrimaryKey(autoGenerate = true) // by default false
    var id: Int = 0
)
```
### Person dao
- Interface z anotacijo Dao
- Upsert (Kombinacija insert in update)
- Uporaba suspend (dobra praksa za baze, da klic baze ne zamrzne UI)
```Kotlin
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

```
### MyDatabase
- Anotacija Database, notri naštete tabele baze
- Vsebuje dao, ki jih bomo želeli klicati
```Kotlin
package com.test.roomtest.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Person::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
}
```
### Primer uporabe
- Pridobitev baze
```Kotlin
val db = Room.databaseBuilder(
    applicationContext,
    MyDatabase::class.java, "test-database"
).build()
```
- Priporočilo: uporaba singelton dp. v programu (saj lahko več dostop do iste baze pripelje do težav)

- Pridobimo dao
```Kotlin
val personDao = db.personDao()
```
- Zaradi uporabe suspend zdaj to rabim kljicat v drugi suspend funkciji ali pa uporabiti nekaj takšnjega kar je lifcycleScope.lunch
- Potem pa lahk preprosto uporabljamo funkcije iz dao interface
```Kotlin
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
}
```
<img width="419" height="171" alt="image" src="https://github.com/user-attachments/assets/b94c37ec-e5d5-44c4-bcd6-a535581a1e50" />
<img width="766" height="240" alt="image" src="https://github.com/user-attachments/assets/5992b6aa-9349-4bea-bdd9-211307c35a7e" />

### Testiranje baze
- Priporočam uporabo **App Inspector** bolj specifično [database inspector](https://developer.android.com/studio/inspect/database) v **Android Studio**
    - Lahko preprosto vidimo shemo baze
    - Omogoča izvoz podatkov tako v sql datoteki kot tudi csv
    - Ima pa tudi zmožnost izvajanja sql querijev

## Možne izjeme
- **IllegalStateException** v primeru da Room ne najde migracijske poti da spremeni obstoječo bazo na napravi v trenutno verzije, ali pa recimo poganjanje na glavni niti brez dovoljenj
- **SQLiteConstraintException** klasične SQL napake(vstavlanje z že obstoječim id, Not null napake, podvajanje ko nastavljen unique)
- **SQLiteException** neveljani queriji
- **IllegalArgumentException** neobstoječ primarni ključ
- **EmptyResultSetException** najdemo nič ko dao zahteva nenullable rezultat
- **SQLiteDatabaseCorruptException** če neveljavna datoteka baze
- **SQLiteReadOnlyDatabaseException** pišemo v readonly bazo

### Sources
- https://developer.android.com/training/data-storage/room
- https://developer.android.com/jetpack/androidx/releases/room
- https://medium.com/@anandgaur2207/room-database-in-android-d5f279d4648a
- https://www.youtube.com/watch?v=bOd3wO0uFr8
- https://developer.android.com/training/data-storage/room/migrating-db-versions#test
- https://medium.com/@dugguRK/room-db-vs-sqlite-f62a9ebdc742
