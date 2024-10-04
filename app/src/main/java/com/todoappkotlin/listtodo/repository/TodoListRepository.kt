package com.todoappkotlin.listtodo.repository

import com.todoappkotlin.room.CategoryDao
import com.todoappkotlin.room.TodoWithCategory

class TodoListRepository(private  val categoryDao: CategoryDao) {

    suspend fun getTodoList(): List<TodoWithCategory>{
        return  categoryDao.getTodoTasksWithCategory()
    }
}