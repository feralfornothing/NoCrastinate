package com.example.nocrastinate

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class TasksFragment : Fragment() {
    private lateinit var item: EditText
    private lateinit var add: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private var activeTasks = ArrayList<Task>()
    private val taskManager = TaskManager()
    private lateinit var notificationHelper: NotificationHelper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Alerty włączone", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationHelper = NotificationHelper(this)
        checkNotificationPermission()

        item = view.findViewById(R.id.czynnosc)
        add = view.findViewById(R.id.zapisz)
        recyclerView = view.findViewById(R.id.lista)

        loadTasks()
        setupRecyclerView()
        setupSwipeToDelete()

        add.setOnClickListener {
            val taskName = item.text.toString()
            if (taskName.isNotEmpty()) {
                val newTask = Task(taskName)
                activeTasks.add(newTask)
                adapter.notifyItemInserted(activeTasks.size - 1)
                item.setText("")
                saveTasks()

                notificationHelper.askForReminder(newTask) { reminderTime ->
                    newTask.reminderTime = reminderTime
                    saveTasks()
                }
            }
        }
    }

    private fun loadTasks() {
        activeTasks = taskManager.loadActiveTasks(requireContext())
    }

    private fun saveTasks() {
        taskManager.saveActiveTasks(requireContext(), activeTasks)
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            activeTasks,
            onTaskClick = { task ->
                notificationHelper.askForReminder(task) { reminderTime ->
                    task.reminderTime = reminderTime
                    saveTasks()
                }
            },
            onTaskComplete = { task, position ->
                val completedTasks = taskManager.loadCompletedTasks(requireContext())
                completedTasks.add(task)
                taskManager.saveCompletedTasks(requireContext(), completedTasks)

                activeTasks.removeAt(position)
                adapter.notifyItemRemoved(position)
                saveTasks()

                Snackbar.make(recyclerView, "Zadanie ukończone!", Snackbar.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun setupSwipeToDelete() {
        SwipeToDeleteHelper { position ->
            val deletedTask = activeTasks[position]
            adapter.removeItem(position)
            saveTasks()

            Snackbar.make(
                recyclerView,
                "Usunięto: ${deletedTask.name}",
                Snackbar.LENGTH_LONG
            ).setAction("COFNIJ") {
                adapter.restoreItem(deletedTask, position)
                saveTasks()
            }.show()
        }.attachTo(recyclerView)
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!notificationHelper.hasNotificationPermission()) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}