package com.example.easyfood.db

import android.content.Context
import androidx.room.*
import com.example.easyfood.pojo.Meal

@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(MealTypeConvertor::class)
abstract class MealDataBase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        var INSTANCE: MealDataBase? = null

        @Synchronized
        fun getInstance(context: Context): MealDataBase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDataBase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as MealDataBase
        }

    }
}