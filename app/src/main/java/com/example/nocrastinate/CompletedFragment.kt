package com.example.nocrastinate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class CompletedFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var adapter: TaskAdapter
    private var completedTasks = ArrayList<Task>()
    private val taskManager = TaskManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_completed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.completedList)
        emptyText = view.findViewById(R.id.emptyText)

        loadTasks()
        setupRecyclerView()
        setupSwipeToDelete()
        updateEmptyState()
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
        adapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun loadTasks() {
        completedTasks = taskManager.loadCompletedTasks(requireContext())
    }

    private fun saveTasks() {
        taskManager.saveCompletedTasks(requireContext(), completedTasks)
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            completedTasks,
            onTaskClick = {},
            onTaskComplete = { _, _ -> },
            showCheckbox = false
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun setupSwipeToDelete() {
        SwipeToDeleteHelper { position ->
            val deletedTask = completedTasks[position]
            adapter.removeItem(position)
            saveTasks()
            updateEmptyState()

            Snackbar.make(
                recyclerView,
                "Usunięto: ${deletedTask.name}",
                Snackbar.LENGTH_LONG
            ).setAction("Cofnij") {
                adapter.restoreItem(deletedTask, position)
                saveTasks()
                updateEmptyState()
            }.show()
        }.attachTo(recyclerView)
    }

    private fun updateEmptyState() {
        if (completedTasks.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
        }
    }
}