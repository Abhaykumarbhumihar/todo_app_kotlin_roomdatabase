# Todo App - Kotlin (MVVM, Room, Coroutines)

## Overview

This is a simple **Todo List Application** developed in Kotlin, following the **MVVM architecture** pattern. The app uses **Room Database** with **Foreign Keys** to organize Todo tasks by **Categories**. It also utilizes **Kotlin Coroutines** for background processing and smooth operation of database transactions.

## Features

- Add, update, and list **Categories**.
- Add, update, and list **Todo Tasks**.
- Organize Todo tasks under specific **Categories** using Foreign Key relationships.
- Mark tasks as complete/incomplete.
- Filter tasks based on **Status** (completed/pending).
- Filter tasks by **Category**.

## Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room
- **Concurrency**: Kotlin Coroutines

## Database Structure

The app has two main entities:

1. **CategoryEntity**: Represents a category for organizing tasks.
2. **TodoEntity**: Represents a task with a foreign key reference to a category.

### CategoryEntity

```kotlin
@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String
)
```


## TodoEntity
```kotlin
@Entity(
    tableName = "todotask",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val taskName: String,
    val date: String,
    val time: String,
    val categoryId: Int,
    val status: Boolean
)
```

## TodoWithCategory (Data Class for Relationship)
```kotlin
data class TodoWithCategory(
    @Embedded val todo: TodoEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity
)
```
## DAO Operations
```kotlin
@Dao
interface CategoryDao {

    // Insert a Category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryEntity: CategoryEntity)

    // Retrieve all categories
    @Query("SELECT * FROM category")
    suspend fun getAllCategory(): List<CategoryEntity>

    // Insert a Todo task
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoTask(todoEntity: TodoEntity)

    // Retrieve all Todo tasks with their associated categories
    @Transaction
    @Query("SELECT * FROM todotask")
    suspend fun getTodoTasksWithCategory(): List<TodoWithCategory>

    // Update the status of a Todo task
    @Query("UPDATE todotask SET status = :status WHERE id = :todoId")
    suspend fun updateTodoStatus(todoId: Int, status: Boolean)

    // Retrieve pending Todo tasks
    @Query("SELECT * FROM todotask WHERE status = 0")
    suspend fun getPendingTodoTasks(): List<TodoWithCategory>

    // Retrieve tasks by status (completed/pending)
    @Query("SELECT * FROM todotask WHERE status = :status")
    suspend fun getTodoTasksByStatus(status: Boolean): List<TodoWithCategory>

    // Retrieve tasks by category
    @Transaction
    @Query("SELECT * FROM todotask WHERE categoryId = :categoryId")
    suspend fun getTodoTasksByCategoryId(categoryId: Int): List<TodoWithCategory>

    // Update a Todo task's fields
    @Query("UPDATE todotask SET taskName = :taskName, date = :date, time = :time, categoryId = :categoryId, status = :status WHERE id = :todoId")
    suspend fun updateTodo(
        todoId: Int,
        taskName: String,
        date: String,
        time: String,
        categoryId: Int,
        status: Boolean
    )
}
```
## AppDataBase
```kotlin
@Database(entities = [CategoryEntity::class, TodoEntity::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    companion object {

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDataBase::class.java, "todo_database"
                )

                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

## ViewModel
```kotlin
class TodoListViewModel(private val respositroy: TodoListRepository) : ViewModel() {

    fun getActiveTodoList(callBack: TodoListCallBack<List<TodoWithCategory>>) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val list = respositroy.getTodoList()
                callBack.onSuccess(list)

            } catch (e: Exception) {
                callBack.onError(e)
            }
        }
    }


    fun updateTodoStatus(todoId: Int, status: Boolean, callback: TodoListCallBack<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                respositroy.updateTodoStatus(todoId, status)
                if(status){
                    callback.onSuccess("Task updated as complete")
                }else{
                    callback.onSuccess("Task updated as in-complete")
                }

            } catch (e: Exception) {
                callback.onError(e)
            }
        }
    }

}
```

## TodoListViewModelFactory
```kotlin
class TodoListViewModelFactory(private val repository: TodoListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            return TodoListViewModel(respositroy = repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}
```



