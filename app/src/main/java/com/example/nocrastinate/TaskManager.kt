package com.example.nocrastinate

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskManager {
    private val gson = Gson()
    private val ACTIVE_TASKS_FILE = "active_tasks.json"
    private val COMPLETED_TASKS_FILE = "completed_tasks.json"

    fun saveActiveTasks(context: Context, tasks: ArrayList<Task>) {
        val json = gson.toJson(tasks)
        context.openFileOutput(ACTIVE_TASKS_FILE, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    fun loadActiveTasks(context: Context): ArrayList<Task> {
        return try {
            val json = context.openFileInput(ACTIVE_TASKS_FILE).bufferedReader().use { it.readText() }
            val type = object : TypeToken<ArrayList<Task>>() {}.type
            gson.fromJson(json, type) ?: ArrayList()
        } catch (e: Exception) {
            ArrayList()
        }
    }

    fun saveCompletedTasks(context: Context, tasks: ArrayList<Task>) {
        val json = gson.toJson(tasks)
        context.openFileOutput(COMPLETED_TASKS_FILE, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    fun loadCompletedTasks(context: Context): ArrayList<Task> {
        return try {
            val json = context.openFileInput(COMPLETED_TASKS_FILE).bufferedReader().use { it.readText() }
            val type = object : TypeToken<ArrayList<Task>>() {}.type
            gson.fromJson(json, type) ?: ArrayList()
        } catch (e: Exception) {
            ArrayList()
        }
    }

    fun clearCompletedTasks(context: Context) {
        saveCompletedTasks(context, ArrayList())
    }

    fun clearAllTasks(context: Context) {
        saveActiveTasks(context, ArrayList())
        saveCompletedTasks(context, ArrayList())
    }
}