package com.example.mytodos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<Todo> todoList;
    private OnTodoClickListener listener;

    public interface OnTodoClickListener {
        void onTodoClick(int position);
        void onTodoLongClick(int position);
        void onCheckboxChanged(int position, boolean isChecked);
    }

    public TodoAdapter(List<Todo> todoList, OnTodoClickListener listener) {
        this.todoList = todoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todoList.get(position);

        holder.tvTitle.setText(todo.getTitle());
        holder.tvDescription.setText(todo.getDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        if (todo.getDeadline()) {
            holder.tvDeadline.setText("Deadline: " + sdf.format(todo.getDeadline()));
        } else {
            holder.tvDeadline.setText("Tidak ada deadline");
        }

        holder.cbCompleted.setChecked(todo.isCompleted());
    }
}
