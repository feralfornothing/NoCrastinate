package com.example.nocrastinate

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: ArrayList<Task>,
    private val onTaskClick: (Task) -> Unit,
    private val onTaskComplete: (Task, Int) -> Unit,
    private val showCheckbox: Boolean = true
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskText: TextView = view.findViewById(R.id.taskText)
        val checkbox: CheckBox? = view.findViewById(R.id.taskCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutId = if (showCheckbox) R.layout.item_task_checkbox else R.layout.item_task
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskText.text = task.name

        if (showCheckbox) {
            holder.checkbox?.isChecked = task.isCompleted
            holder.checkbox?.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                if (isChecked) {
                    holder.taskText.paintFlags = holder.taskText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    onTaskComplete(task, position)
                } else {
                    holder.taskText.paintFlags = holder.taskText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }

            if (task.isCompleted) {
                holder.taskText.paintFlags = holder.taskText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.taskText.paintFlags = holder.taskText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        holder.itemView.setOnClickListener {
            onTaskClick(task)
        }
    }

    override fun getItemCount() = tasks.size

    fun removeItem(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(task: Task, position: Int) {
        tasks.add(position, task)
        notifyItemInserted(position)
    }
}