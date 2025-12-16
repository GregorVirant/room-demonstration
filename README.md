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
- Ne potrebuje nobenih posebnih dovoljenj.
## Osnovna zgradba
... Mogoc sam ob primeru
<img width="724" height="619" alt="image" src="https://github.com/user-attachments/assets/86178186-1338-4bce-9ac3-d4159fa89d35" />

## Setup
- [Preberi več](https://developer.android.com/training/data-storage/room#setup)
- Moj primer podpira samo kotlin z `build.gradle.kts`
- V `build.gradle.kts` od `app`
```
dependencies {
    val room_version = "2.8.4" // My version (current latest)
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version") // OPTIONAL For kotlin coroutines
    ksp("androidx.room:room-compiler:$room_version") // Kotlin Symbol Processing (annotation processor)
}
```
- Potreben je tudi [ksp](https://developer.android.com/build/migrate-to-ksp#add-ksp)

## Primer Uporabe
## Možne izjeme
- V primeru da Room ne najde migracijske poti da spremeni obstoječo bazo na napravi v trenutno verzije vrže **IllegalStateException**

### Sources
- https://developer.android.com/training/data-storage/room
- https://developer.android.com/jetpack/androidx/releases/room
- https://medium.com/@anandgaur2207/room-database-in-android-d5f279d4648a
- https://www.youtube.com/watch?v=bOd3wO0uFr8
- https://developer.android.com/training/data-storage/room/migrating-db-versions#test
- https://medium.com/@dugguRK/room-db-vs-sqlite-f62a9ebdc742
