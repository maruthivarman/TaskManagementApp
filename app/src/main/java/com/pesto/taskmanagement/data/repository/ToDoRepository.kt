package com.pesto.taskmanagement.data.repository

import androidx.lifecycle.LiveData
import com.pesto.taskmanagement.data.ToDoDao
import com.pesto.taskmanagement.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) = toDoDao.insertData(toDoData)

    suspend fun updateData(toDoData: ToDoData) = toDoDao.updateData(toDoData)

    suspend fun deleteItem(toDoData: ToDoData) = toDoDao.deleteItem(toDoData)

    suspend fun deleteAll() = toDoDao.deleteAll()

    fun searchDatabase(searchQuery: String) = toDoDao.searchDatabase(searchQuery)

    fun sortByToDoStatus() = toDoDao.sortByToDoStatus()

    fun sortByInProgressStatus() = toDoDao.sortByInProgressStatus()

    fun sortByDoneStatus() = toDoDao.sortByDoneStatus()
}