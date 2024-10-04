package com.todoappkotlin.addtask.callback


interface CategoryCallback<T> {
    fun onSuccess(categories: T)
    fun onError(exception: Exception)
}