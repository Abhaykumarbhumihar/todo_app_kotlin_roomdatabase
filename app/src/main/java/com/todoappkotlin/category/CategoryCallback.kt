package com.todoappkotlin.category


interface CategoryCallback<T> {
    fun onSuccess(categories: T)
    fun onError(exception: Exception)
}