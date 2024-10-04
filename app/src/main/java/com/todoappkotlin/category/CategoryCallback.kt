package com.todoappkotlin.category

import com.todoappkotlin.room.CategoryEntity

interface CategoryCallback<T> {
    fun onSuccess(categories: T)
    fun onError(exception: Exception)

}