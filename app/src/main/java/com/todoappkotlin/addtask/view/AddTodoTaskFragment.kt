package com.todoappkotlin.addtask.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.todoappkotlin.addtask.callback.CategoryCallback
import com.todoappkotlin.addtask.repository.AddTaskRepository
import com.todoappkotlin.addtask.view.adapter.CategorySpinnerAdapter
import com.todoappkotlin.addtask.view.dialog.CategoryDialogFragment
import com.todoappkotlin.addtask.viewmodel.AddTaskViewModelFactory
import com.todoappkotlin.addtask.viewmodel.AddTaskViewModel
import com.todoappkotlin.databinding.FragmentActiveTodoListBinding
import com.todoappkotlin.room.AppDataBase
import com.todoappkotlin.room.CategoryEntity
import com.todoappkotlin.room.TodoEntity
import java.util.Calendar


class AddTodoTaskFragment : Fragment(), CategoryDialogFragment.CategoryDialogListener {
    private var _binding: FragmentActiveTodoListBinding? = null
    private val binding get() = _binding!!

    private var dueDateSelected: Boolean = false
    private var timeSelected: Boolean = false


    private lateinit var cateogryViewModel: AddTaskViewModel
    private lateinit var categoryRepository: AddTaskRepository

    lateinit var categorylist: List<CategoryEntity>

    private var isCategorySelected = false // Track if a valid category is selected
    private var lastSelectedPosition = 0 // Track the last selected position

    var selectedCategoryIdForSaveInTodo: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentActiveTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
        val categoryDao = context?.let { AppDataBase.getDatabase(it).categoryDao() }
        categoryRepository = categoryDao?.let { AddTaskRepository(it) }!!

        val viewModelFactory = AddTaskViewModelFactory(categoryRepository)
        cateogryViewModel = ViewModelProvider(this, viewModelFactory)[AddTaskViewModel::class.java]

        refreshCategoryList()

        // Initially hide time layout and clear buttons
        binding.timelayout.visibility = View.GONE
        binding.clearduedate.visibility = View.GONE
        binding.cleartime.visibility = View.GONE


        // Select Due Date
        binding.selectduedate.setOnClickListener {
            showDatePicker()
        }

        // Clear Due Date
        binding.clearduedate.setOnClickListener {
            clearDueDate()
        }

        // Select Time
        binding.selecttime.setOnClickListener {
            showTimePicker()
        }

        // Clear Time
        binding.cleartime.setOnClickListener {
            clearTime()
        }

        //add list name dialog
        binding.addCateogry.setOnClickListener {
            showAddCategoryDialog()
        }

        binding.llAddtask.setOnClickListener {
            val taskname = binding.edtaskname.text.toString()
            if (taskname.isEmpty()) {
                Toast.makeText(context, "Please enter task name", Toast.LENGTH_LONG).show()

            } else {
                val duedate = binding.tvduedate.text.toString() + ""
                val time = binding.tvtime.text.toString() + ""
                val todo = TodoEntity(
                    taskName = "$taskname",
                    categoryId = selectedCategoryIdForSaveInTodo!!,
                    date = duedate,
                    time = time
                )
                cateogryViewModel.addTodoTask(todo, object : CategoryCallback<String> {
                    override fun onSuccess(categories: String) {
                        requireActivity().runOnUiThread {
                            Toast.makeText(context, "Task is added", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(exception: Exception) {
                        requireActivity().runOnUiThread {
                            Log.w("ONERROR ", exception.message!!)
                        }
                    }
                })
            }
        }
        setupSpinnerListener()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Set selected due date
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                binding.tvduedate.text = selectedDate
                dueDateSelected = true
                updateVisibility()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(), { _, hourOfDay, minute ->
                // Set selected due time
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                binding.tvtime.text = selectedTime
                timeSelected = true
                binding.cleartime.visibility = View.VISIBLE // Show clear time button
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        )
        timePickerDialog.show()
    }

    private fun updateVisibility() {
        // Show or hide timelayout and clear buttons based on selections
        binding.timelayout.visibility = if (dueDateSelected) View.VISIBLE else View.GONE
        binding.clearduedate.visibility = if (dueDateSelected) View.VISIBLE else View.GONE
        if (!dueDateSelected) {
            timeSelected = false
            binding.cleartime.visibility = View.GONE // Hide clear time button
            binding.tvtime.text = "" // Clear time text
        }
    }

    private fun clearDueDate() {
        dueDateSelected = false
        binding.tvduedate.text = "" // Clear due date text
        updateVisibility()
    }

    private fun clearTime() {
        timeSelected = false
        binding.tvtime.text = "" // Clear time text
        binding.cleartime.visibility = View.GONE // Hide clear time button
    }

    private fun refreshCategoryList() {
        cateogryViewModel.getCategoryList(object : CategoryCallback<List<CategoryEntity>> {
            override fun onSuccess(categories: List<CategoryEntity>) {
                requireActivity().runOnUiThread {
                    categorylist = categories // Store the categories for later use
                    val categoryNames = mutableListOf("Select Task Category") // Add a placeholder
                    val categoryEntities = mutableListOf(CategoryEntity(-0, "Select Task Category"))
                    categoryEntities.addAll(categories) // Add actual categories to the list

                    setupCategorySpinner(categoryEntities) // Set up spinner with CategoryEntity objects

                }
            }

            override fun onError(exception: Exception) {
                requireActivity().runOnUiThread {
                    Log.w("CHECK ERROR ", exception.message!!)
                    Toast.makeText(
                        context, "Error: ${exception.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setupCategorySpinner(categoryNames: List<CategoryEntity>) {
        val adapter = CategorySpinnerAdapter(requireContext(), categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter // Set the adapter to the Spinner
    }

    private fun showAddCategoryDialog() {
        val dialog = CategoryDialogFragment()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "CategoryDialogFragment")
    }

    private fun setupSpinnerListener() {
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    // Cast the selected item to CategoryEntity
                    val selectedCategory = parent.getItemAtPosition(position) as CategoryEntity

                    // If the placeholder is selected
                    if (selectedCategory.categoryName == "Select Task Category" && !isCategorySelected) {
                        // Do nothing; it's the initial selection
                    } else if (selectedCategory.categoryName == "Select Task Category" && isCategorySelected) {
                        // Show a message that the placeholder cannot be selected again
                        Toast.makeText(
                            requireContext(),
                            "You cannot select 'Select Task Category' again.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Re-select the last valid category (find the last selected category)
                        binding.categorySpinner.setSelection(getLastSelectedPosition())
                    } else {
                        // Valid category selected
                        isCategorySelected = true // Mark that a valid category has been selected

                        // Get the ID of the selected CategoryEntity
                        val selectedCategoryId = selectedCategory.id
                        // You can now use selectedCategoryId for further operations

                        // Example: Printing the selected category ID
                        Toast.makeText(
                            requireContext(),
                            "Selected Category ID: $selectedCategoryId",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Store or use the selectedCategory for further actions
                        selectedCategoryIdForSaveInTodo = selectedCategory.id
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Handle case when nothing is selected (optional)
                }
            }
    }


    private fun getLastSelectedPosition(): Int {
        return lastSelectedPosition
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference
    }

    override fun onCategoryAdded(categoryName: String) {
        addCateogryData(categoryName)
    }


    private fun addCateogryData(categoryName: String) {
        val newCategory = CategoryEntity(categoryName = "$categoryName")
        cateogryViewModel.addCategory(newCategory, object : CategoryCallback<String> {
            override fun onSuccess(categories: String) {
                requireActivity().runOnUiThread {
                    refreshCategoryList()
                }
            }

            override fun onError(exception: Exception) {
                requireActivity().runOnUiThread {

                    Toast.makeText(
                        context, "Error: ${exception.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}