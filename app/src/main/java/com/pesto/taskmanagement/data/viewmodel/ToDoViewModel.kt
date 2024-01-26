package com.pesto.taskmanagement.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pesto.taskmanagement.data.ToDoDatabase
import com.pesto.taskmanagement.data.models.ToDoData
import com.pesto.taskmanagement.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application): AndroidViewModel(application) {
    private val todoDao = ToDoDatabase.getDatabase(application).todoDao()
    private val repository: ToDoRepository = ToDoRepository(todoDao)
    val getAllData: LiveData<List<ToDoData>> = repository.getAllData
    val sortedDataToDo: LiveData<List<ToDoData>> = repository.sortByToDoStatus()
    val sortedDataInProgress: LiveData<List<ToDoData>> = repository.sortByInProgressStatus()
    val sortedDataDone: LiveData<List<ToDoData>> = repository.sortByDoneStatus()

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchDatabase(searchQuery: String) = repository.searchDatabase(searchQuery)
}