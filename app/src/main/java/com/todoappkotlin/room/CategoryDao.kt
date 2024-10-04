package com.todoappkotlin.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertCategory(categoryEntity: CategoryEntity)


    @Query("SELECT * FROM category")
    suspend fun  getAllCategory():  List<CategoryEntity>

}