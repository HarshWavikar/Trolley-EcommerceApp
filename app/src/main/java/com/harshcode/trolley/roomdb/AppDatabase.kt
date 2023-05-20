package com.harshcode.trolley.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductR::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

//This is to create single instance of database
    companion object {
        private var database: AppDatabase? = null
        private val DATABASE_NAME = "trolley"

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (database == null) {
                database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return database!!
        }
    }

    abstract fun productDao(): ProductDAO

}