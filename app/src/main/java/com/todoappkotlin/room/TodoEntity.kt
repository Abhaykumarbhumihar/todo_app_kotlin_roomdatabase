package com.todoappkotlin.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "todotask",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class, // Refers to CategoryEntity
            parentColumns = ["id"], // The column in CategoryEntity (its ID)
            childColumns = ["categoryId"], // The column in TodoEntity that will store the reference (categoryId)
            onDelete = ForeignKey.CASCADE // If category is deleted, related tasks will also be deleted
        )
    ],
    indices = [Index(value = ["categoryId"])] // This improves the speed of queries involving categoryId
)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-generated ID for task
    val date: String = "", // Task date
    val time: String = "", // Task time
    val taskName: String, // Name of the task
    val categoryId: Int, // Foreign key (the ID of a CategoryEntity)
    var status:Boolean=false //Task status (false means pending true means complete)
)



data class TodoWithCategory(
    @Embedded val todo: TodoEntity, // Contains the task details
    @Relation(
        parentColumn = "categoryId", // Links the categoryId from TodoEntity
        entityColumn = "id" // Links to the id in CategoryEntity
    )
    val category: CategoryEntity // Contains the category details
)