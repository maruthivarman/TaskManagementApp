package com.pesto.taskmanagement.fragments

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pesto.taskmanagement.R
import com.pesto.taskmanagement.data.models.Status
import com.pesto.taskmanagement.data.models.ToDoData

class SharedViewModel(application: Application): AndroidViewModel(application) {

	val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

	fun isDatabaseEmpty(toDoData: List<ToDoData>) {
		emptyDatabase.value = toDoData.isEmpty()
	}

	val listener: AdapterView.OnItemSelectedListener = object:
		AdapterView.OnItemSelectedListener {
			override fun onItemSelected(
				parent: AdapterView<*>?,
				view: View?,
				position: Int,
				id: Long
			) {
				when(position) {
					0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))}
					1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow))}
					2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))}
				}
			}

			override fun onNothingSelected(
				parent: AdapterView<*>?
			) {

			}
		}

	fun verifyDataFromUser(title: String, desc: String): Boolean {
		return !(title.isNullOrEmpty() || desc.isNullOrEmpty())
	}

	fun parseStatusString(status: String): Status {
		return when(status) {
			"ToDo" -> Status.ToDo
			"Done" -> Status.Done
			"In Progress" -> Status.InProgress
			else -> Status.Done
		}
	}
}