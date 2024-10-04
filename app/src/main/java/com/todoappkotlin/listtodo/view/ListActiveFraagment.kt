package com.todoappkotlin.listtodo.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.todoappkotlin.R
import com.todoappkotlin.addtask.view.AddTodoTaskFragment
import com.todoappkotlin.databinding.FragmentListActiveFraagmentBinding
import com.todoappkotlin.listtodo.callback.TodoListCallBack
import com.todoappkotlin.listtodo.repository.TodoListRepository
import com.todoappkotlin.listtodo.view.adapter.TodoAdapter
import com.todoappkotlin.listtodo.viewmodel.TodoListViewModel
import com.todoappkotlin.listtodo.viewmodel.TodoListViewModelFactory
import com.todoappkotlin.room.AppDataBase
import com.todoappkotlin.room.TodoWithCategory


class ListActiveFragment : Fragment() {

    private lateinit var todoListViewModel: TodoListViewModel
    private lateinit var todoListRepository: TodoListRepository
    private lateinit var todoAdapter: TodoAdapter

    private var _binding: FragmentListActiveFraagmentBinding? = null

    lateinit var adapterlistener: TodoAdapter.OnTodoItemCheckedChangeListener
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListActiveFraagmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Todo Work"
        setHasOptionsMenu(true)


        adapterlistener = object : TodoAdapter.OnTodoItemCheckedChangeListener {
            override fun onTodoItemCheckedChange(
                todoId: Int,
                isChecked: Boolean,
                currentData: TodoWithCategory
            ) {

                updateStatusOfTask(todoId, isChecked)


            }
        }
        // Initialize RecyclerView and Adapter
        todoAdapter = TodoAdapter(emptyList(), adapterlistener) // Start with an empty list
        binding.rvtodolist.layoutManager = LinearLayoutManager(requireContext())
        binding.rvtodolist.adapter = todoAdapter

        // Database setup
        val categoryDao = context?.let { AppDataBase.getDatabase(it).categoryDao() }
        todoListRepository = categoryDao?.let { TodoListRepository(it) }!!

        val viewModelProvider = TodoListViewModelFactory(todoListRepository)
        todoListViewModel =
            ViewModelProvider(this, viewModelProvider)[TodoListViewModel::class.java]

        getTodoList()
    }


    private fun updateStatusOfTask(todoId: Int, status: Boolean) {
        todoListViewModel.updateTodoStatus(todoId, status, object : TodoListCallBack<String> {
            override fun onSuccess(categories: String) {

                requireActivity().runOnUiThread {
                    Toast.makeText(context, categories, Toast.LENGTH_LONG).show()
                    getTodoList()
                }
            }

            override fun onError(exception: Exception) {
                requireActivity().runOnUiThread {
                    Toast.makeText(context, exception.message!!, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        onStart()
    }

    private fun getTodoList() {
        Log.w("SDF DSF DSF DFS", "SDF SDF SDF DSF DSF ")
        todoListViewModel.getActiveTodoList(object : TodoListCallBack<List<TodoWithCategory>> {
            override fun onSuccess(categories: List<TodoWithCategory>) {
                requireActivity().runOnUiThread {
                    Log.w("LIST ACTIVE  onSuccess", "${categories.size}")
                    categories.let {
                        todoAdapter.setTodoList(categories)
                    }

                }
            }

            override fun onError(exception: Exception) {
                requireActivity().runOnUiThread {
                    Log.w("LIST ACTIVE  onError", exception.message!!)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_todo -> {
                replaceFragment(AddTodoTaskFragment()) // Handle add todo action
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
        _binding = null // Avoid memory leaks
    }
}
