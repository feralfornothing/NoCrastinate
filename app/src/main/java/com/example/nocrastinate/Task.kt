package com.example.nocrastinate

data class Task(
    val name: String,
    val id: Long = System.currentTimeMillis(),
    var isCompleted: Boolean = false,
    var reminderTime: Long? = null
)