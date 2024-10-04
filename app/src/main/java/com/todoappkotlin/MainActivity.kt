package com.todoappkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.todoappkotlin.category.CategoryCallback
import com.todoappkotlin.category.repository.CategoryRepository
import com.todoappkotlin.category.viewmodel.CategoryViewModelFactory
import com.todoappkotlin.category.viewmodel.CateogryViewModel
import com.todoappkotlin.room.AppDataBase
import com.todoappkotlin.room.CategoryEntity

class MainActivity : AppCompatActivity() {

    private lateinit var cateogryViewModel: CateogryViewModel
    private lateinit var categoryRepository: CategoryRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categoryDao = AppDataBase.getDatabase(applicationContext).categoryDao()
        categoryRepository = CategoryRepository(categoryDao)

        val viewModelFactory = CategoryViewModelFactory(categoryRepository)
        cateogryViewModel =
            ViewModelProvider(this, viewModelFactory)[CateogryViewModel::class.java]

        addCateogryData()

    }

    private fun refreshCategoryList() {
        cateogryViewModel.getCategoryList(object : CategoryCallback<List<CategoryEntity>> {
            override fun onSuccess(categories: List<CategoryEntity>) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Fetched ${categories.size} categories",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onError(exception: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun addCateogryData() {
        val newCategory = CategoryEntity(categoryName = "SDF SDF ")
        cateogryViewModel.addCategory(newCategory, object : CategoryCallback<String> {
            override fun onSuccess(categories: String) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, categories, Toast.LENGTH_SHORT).show()
                    refreshCategoryList()
                }
            }

            override fun onError(exception: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}