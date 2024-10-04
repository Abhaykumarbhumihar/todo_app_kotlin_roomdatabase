package com.todoappkotlin.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CategoryDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertCategory(categoryEntity: CategoryEntity)


    @Query("SELECT * FROM category")
    suspend fun  getAllCategory():  List<CategoryEntity>


    // Insert a Task (TodoEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoTask(todoEntity: TodoEntity)


    @Transaction
    @Query("SELECT * FROM todotask")
    suspend fun getTodoTasksWithCategory(): List<TodoWithCategory>

}