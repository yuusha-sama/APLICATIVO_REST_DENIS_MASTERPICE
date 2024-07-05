package com.example.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.AddTarefa;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.ToDoModel;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.json.JSONObject;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;

    public ToDoAdapter(List<ToDoModel> todoList, MainActivity activity) {
        this.todoList = todoList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoModel item = todoList.get(position);
        holder.todoText.setText(item.getTask());
        holder.todoCheckBox.setChecked(item.getTaskStatus() == 1);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        final int idTask = item.getIdTask();
        new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://datafit.com.br/api/task/TaskApi/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JSONObject taskJson = new JSONObject();
                taskJson.put("id_task", idTask);

                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                os.write(taskJson.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                String responseMessage = urlConnection.getResponseMessage();
                Log.d("ToDoAdapter", "Response Code: " + responseCode);
                Log.d("ToDoAdapter", "Response Message: " + responseMessage);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    activity.runOnUiThread(() -> {
                        todoList.remove(position);
                        notifyItemRemoved(position);
                    });
                } else {
                    Log.e("ToDoAdapter", "Failed to delete task with id: " + idTask);
                }
            } catch (Exception e) {
                Log.e("ToDoAdapter", "Exception during delete task with id: " + idTask, e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }).start();
    }

    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getIdTask());
        bundle.putString("task", item.getTask());
        AddTarefa fragment = new AddTarefa();
        fragment.setArguments(bundle);
        fragment.show(((FragmentActivity) activity).getSupportFragmentManager(), AddTarefa.TAG);
    }

    public Context getContext() {
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox todoCheckBox;
        TextView todoText;

        ViewHolder(View view) {
            super(view);
            todoCheckBox = view.findViewById(R.id.todoCheckBox);
            todoText = view.findViewById(R.id.todoText);
        }
    }
}
