package com.todoappkotlin.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.todoappkotlin.category.repository.CategoryRepository
import java.lang.IllegalArgumentException


class CategoryViewModelFactory (private val  repository: CategoryRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(CateogryViewModel::class.java)){
            return CateogryViewModel(repository) as T
        }
     throw  IllegalArgumentException("Unknow ViewModel class")
    }
}