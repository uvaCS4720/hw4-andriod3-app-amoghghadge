package edu.nd.pmcburne.hello.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Placemark::class, PlacemarkTag::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placemarkDao(): PlacemarkDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "placemarks_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
