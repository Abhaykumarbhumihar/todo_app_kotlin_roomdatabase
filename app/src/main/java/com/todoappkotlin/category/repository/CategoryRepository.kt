package com.todoappkotlin.category.repository

import com.todoappkotlin.room.CategoryDao
import com.todoappkotlin.room.CategoryEntity

class CategoryRepository(private val categoryDao: CategoryDao) {

    suspend fun getCategory(): List<CategoryEntity> {
        return categoryDao.getAllCategory()
    }

    suspend fun addCategory(categoryEntity: CategoryEntity) {
        categoryDao.insertCategory(categoryEntity)
    }
}