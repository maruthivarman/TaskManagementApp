package com.pesto.taskmanagement.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pesto.taskmanagement.data.models.ToDoData

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)

    @Update
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    suspend fun deleteItem(toDoData: ToDoData)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>>

    @Query(
        "SELECT * FROM todo_table ORDER BY CASE " +
        "WHEN status LIKE 'T%' THEN 1 " +
        "WHEN status LIKE 'I%' THEN 2 " +
        "WHEN status LIKE 'D%' THEN 3 END"
    )
    fun sortByToDoStatus(): LiveData<List<ToDoData>>

    @Query(
        "SELECT * FROM todo_table ORDER BY CASE " +
                "WHEN status LIKE 'I%' THEN 1 " +
                "WHEN status LIKE 'T%' THEN 2 " +
                "WHEN status LIKE 'D%' THEN 3 END"
    )
    fun sortByInProgressStatus(): LiveData<List<ToDoData>>

    @Query(
        "SELECT * FROM todo_table ORDER BY CASE " +
        "WHEN status LIKE 'D%' THEN 1 " +
        "WHEN status LIKE 'I%' THEN 2 " +
        "WHEN status LIKE 'T%' THEN 3 END"
    )
    fun sortByDoneStatus(): LiveData<List<ToDoData>>
}