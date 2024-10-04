package com.todoappkotlin.addtask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.todoappkotlin.addtask.repository.AddTaskRepository
import java.lang.IllegalArgumentException


class AddTaskViewModelFactory (private val  repository: AddTaskRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(AddTaskViewModel::class.java)){
            return AddTaskViewModel(repository) as T
        }
     throw  IllegalArgumentException("Unknow ViewModel class")
    }
}