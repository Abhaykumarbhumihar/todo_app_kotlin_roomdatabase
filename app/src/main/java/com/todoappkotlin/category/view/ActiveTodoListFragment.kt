package com.todoappkotlin.category.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.todoappkotlin.R
import com.todoappkotlin.category.CategoryCallback
import com.todoappkotlin.category.repository.CategoryRepository
import com.todoappkotlin.category.view.adapter.CategorySpinnerAdapter
import com.todoappkotlin.category.view.dialog.CategoryDialogFragment
import com.todoappkotlin.category.viewmodel.CategoryViewModelFactory
import com.todoappkotlin.category.viewmodel.CateogryViewModel
import com.todoappkotlin.databinding.FragmentActiveTodoListBinding
import com.todoappkotlin.room.AppDataBase
import com.todoappkotlin.room.CategoryEntity
import java.util.Calendar


class ActiveTodoListFragment : Fragment(), CategoryDialogFragment.CategoryDialogListener {
    private var _binding: FragmentActiveTodoListBinding? = null
    private val binding get() = _binding!!

    private var dueDateSelected: Boolean = false
    private var timeSelected: Boolean = false


    private lateinit var cateogryViewModel: CateogryViewModel
    private lateinit var categoryRepository: CategoryRepository

    lateinit var categorylist: List<CategoryEntity>

    private var isCategorySelected = false // Track if a valid category is selected
    private var lastSelectedPosition = 0 // Track the last selected position

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
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Todo Work"
        setHasOptionsMenu(true)

        val categoryDao = context?.let { AppDataBase.getDatabase(it).categoryDao() }
        categoryRepository = categoryDao?.let { CategoryRepository(it) }!!

        val viewModelFactory = CategoryViewModelFactory(categoryRepository)
        cateogryViewModel = ViewModelProvider(this, viewModelFactory)[CateogryViewModel::class.java]

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


    private fun refreshCategoryList() {
        cateogryViewModel.getCategoryList(object : CategoryCallback<List<CategoryEntity>> {
            override fun onSuccess(categories: List<CategoryEntity>) {
                requireActivity().runOnUiThread {
                    categorylist = categories // Store the categories for later use
                    val categoryNames = mutableListOf("Select Task Category") // Add a placeholder
                    categoryNames.addAll(categories.map { it.categoryName }) // Append actual categories
                    setupCategorySpinner(categoryNames) // Set up spinner with category names

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

    private fun setupCategorySpinner(categoryNames: List<String>) {
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
                    val selectedCategory = parent.getItemAtPosition(position) as String

                    // If the placeholder is selected
                    if (selectedCategory == "Select Task Category" && !isCategorySelected) {
                        // Do nothing; it's the initial selection
                    } else if (selectedCategory == "Select Task Category" && isCategorySelected) {
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
                        // Perform your action based on the selected category

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }


    private fun getLastSelectedPosition(): Int {
        return lastSelectedPosition
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_todo -> {
                replaceFragment(AddTodoFraagment()) // Handle add todo action
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().replace(R.id.framelayout, fragment)
            .addToBackStack(null).commit()
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