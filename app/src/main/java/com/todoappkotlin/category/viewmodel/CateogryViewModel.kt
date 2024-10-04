package com.todoappkotlin.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoappkotlin.category.CategoryCallback
import com.todoappkotlin.category.repository.CategoryRepository
import com.todoappkotlin.room.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CateogryViewModel(private val repository: CategoryRepository) : ViewModel() {


    fun addCategory(category: CategoryEntity, callback: CategoryCallback<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addCategory(category)
                callback.onSuccess("Data added")
            } catch (e: Exception) {
                callback.onError(e)
            }
        }
    }

    fun getCategoryList(callback: CategoryCallback<List<CategoryEntity>>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoryList = repository.getCategory()
                callback.onSuccess(categoryList)
            } catch (e: Exception) {
                callback.onError(e)
            }

        }
    }


}