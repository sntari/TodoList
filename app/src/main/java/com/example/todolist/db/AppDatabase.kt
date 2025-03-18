package com.example.todolist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(TodoEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTodoDao() : TodoDao

    companion object{
        val databaseName = "todo"
        var appDatabase : AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase? {
            if(appDatabase == null){
                appDatabase = Room.databaseBuilder(context,AppDatabase::class.java, databaseName).fallbackToDestructiveMigration().build()
            }
            return appDatabase
        }
    }
}