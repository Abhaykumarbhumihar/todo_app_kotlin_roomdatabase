package com.todoappkotlin.addtask.repository

import com.todoappkotlin.room.CategoryDao
import com.todoappkotlin.room.CategoryEntity
import com.todoappkotlin.room.TodoEntity
import com.todoappkotlin.room.TodoWithCategory

class AddTaskRepository(private val categoryDao: CategoryDao) {

    suspend fun getCategory(): List<CategoryEntity> {
        return categoryDao.getAllCategory()
    }

    suspend fun addCategory(categoryEntity: CategoryEntity) {
        categoryDao.insertCategory(categoryEntity)
    }

    suspend fun addTask(todoEntity: TodoEntity){
        categoryDao.insertTodoTask(todoEntity)
    }

    suspend fun getTodoLis():List<TodoWithCategory>{
        return  categoryDao.getTodoTasksWithCategory()
    }
}