package com.todoappkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.todoappkotlin.category.CategoryCallback
import com.todoappkotlin.category.repository.CategoryRepository
import com.todoappkotlin.category.view.ActiveTodoListFragment
import com.todoappkotlin.category.viewmodel.CategoryViewModelFactory
import com.todoappkotlin.category.viewmodel.CateogryViewModel
import com.todoappkotlin.databinding.ActivityMainBinding
import com.todoappkotlin.room.AppDataBase
import com.todoappkotlin.room.CategoryEntity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var fragmentManager: FragmentManager? = null
    var fragmentTransaction: FragmentTransaction? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addfragment(ActiveTodoListFragment())
    }


    fun addfragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, fragment)
            .addToBackStack(null).commit()
    }

}