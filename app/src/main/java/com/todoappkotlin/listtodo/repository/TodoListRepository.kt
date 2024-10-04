package com.todoappkotlin.listtodo.repository

import com.todoappkotlin.room.CategoryDao
import com.todoappkotlin.room.TodoWithCategory

class TodoListRepository(private  val categoryDao: CategoryDao) {

    suspend fun getTodoList(): List<TodoWithCategory>{
        return  categoryDao.getTodoTasksWithCategory()
    }


    suspend fun updateTodoStatus(todoId: Int, status: Boolean) {
        categoryDao.updateTodoStatus(todoId, status) // Call the DAO method
    }
}