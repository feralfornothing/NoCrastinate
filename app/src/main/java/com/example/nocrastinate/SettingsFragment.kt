package com.example.nocrastinate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {
    private lateinit var totalTasksText: TextView
    private lateinit var activeTasksText: TextView
    private lateinit var completedTasksText: TextView
    private lateinit var clearCompletedBtn: Button
    private lateinit var clearAllBtn: Button
    private val taskManager = TaskManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalTasksText = view.findViewById(R.id.totalTasksText)
        activeTasksText = view.findViewById(R.id.activeTasksText)
        completedTasksText = view.findViewById(R.id.completedTasksText)
        clearCompletedBtn = view.findViewById(R.id.clearCompletedBtn)
        clearAllBtn = view.findViewById(R.id.clearAllBtn)

        updateStats()

        clearCompletedBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Wyczyść ukończone")
                .setMessage("Czy chcesz usunąć wszystkie ukończone zadania?")
                .setPositiveButton("Tak") { _, _ ->
                    taskManager.clearCompletedTasks(requireContext())
                    updateStats()
                    Toast.makeText(requireContext(), "Ukończone zadania usunięte", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Nie", null)
                .show()
        }

        clearAllBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Wyczyść wszystko")
                .setMessage("Czy chcesz usunąć WSZYSTKIE zadania? Ta operacja jest nieodwracalna!")
                .setPositiveButton("Tak") { _, _ ->
                    taskManager.clearAllTasks(requireContext())
                    updateStats()
                    Toast.makeText(requireContext(), "Wszystkie zadania usunięte", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Nie", null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        updateStats()
    }

    private fun updateStats() {
        val activeTasks = taskManager.loadActiveTasks(requireContext())
        val completedTasks = taskManager.loadCompletedTasks(requireContext())
        val total = activeTasks.size + completedTasks.size

        totalTasksText.text = "Wszystkie: $total"
        activeTasksText.text = "Oczekujące: ${activeTasks.size}"
        completedTasksText.text = "Ukończone: ${completedTasks.size}"
    }
}