package com.example.mealmate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mealmate.data.dao.UserDao
import com.example.mealmate.data.entity.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class MealMateDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: MealMateDatabase? = null

        fun getDatabase(context: Context): MealMateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealMateDatabase::class.java,
                    "mealmate_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 