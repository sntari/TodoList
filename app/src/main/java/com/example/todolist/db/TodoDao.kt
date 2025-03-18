package com.example.todolist.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {

    // get ALL
    @Query("SELECT * FROM TodoEntity")
    fun getAllTodo() : List<TodoEntity>

    // insert todo
    @Insert
    fun insertTodo(todo : TodoEntity)

    // delete todo
    @Delete
    fun deleteTodo(todo : TodoEntity)
}