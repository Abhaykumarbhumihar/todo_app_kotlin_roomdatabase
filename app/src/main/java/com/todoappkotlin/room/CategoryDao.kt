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
    suspend fun getAllCategory(): List<CategoryEntity>

    // Insert a Task (TodoEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoTask(todoEntity: TodoEntity)

    @Transaction
    @Query("SELECT * FROM todotask")
    suspend fun getTodoTasksWithCategory(): List<TodoWithCategory>

    @Query("UPDATE todotask SET status = :status WHERE id = :todoId")
    suspend fun updateTodoStatus(todoId: Int, status: Boolean)

    @Query("SELECT * FROM todotask WHERE status = 0")//0 or 1 to represent false and true
    suspend fun getPendingTodoTasks(): List<TodoWithCategory>

    /*for filter task based on requirement*/
    @Query("SELECT * FROM todotask WHERE status = :status")
    suspend fun getTodoTasksByStatus(status: Boolean): List<TodoWithCategory>


    @Transaction
    @Query("SELECT * FROM todotask WHERE categoryId = :categoryId")
    suspend fun getTodoTasksByCategoryId(categoryId: Int): List<TodoWithCategory>

}