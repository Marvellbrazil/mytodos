package com.example.mytodos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TodoDAO {
    @Insert
    void insert(Todo todo);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("SELECT * FROM todos ORDER BY deadline ASC")
    List<Todo> getAllTodos();

    @Query("SELECT * FROM todos WHERE isCompleted = 0 ORDER BY deadline ASC")
    List<Todo> getActiveTodos();

    @Query("SELECT * FROM todos WHERE isCompleted = 1 ORDER BY deadline ASC")
    List<Todo> getCompletedTodos();
}