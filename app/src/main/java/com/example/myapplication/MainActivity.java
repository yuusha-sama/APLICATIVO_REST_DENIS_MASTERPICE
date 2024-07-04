package com.example.myapplication;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters   .ToDoAdapter;
import com.example.myapplication.model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        tasksAdapter = new ToDoAdapter(taskList);
        tasksRecyclerView.setAdapter(tasksAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTarefa.newInstance().show(getSupportFragmentManager(), AddTarefa.TAG);
            }
        });

        fetchTasks();
    }

    private void fetchTasks() {
        new FetchTasksTask().execute();
    }

    private class FetchTasksTask extends AsyncTask<Void, Void, List<ToDoModel>> {
        @Override
        protected List<ToDoModel> doInBackground(Void... voids) {
            List<ToDoModel> tasks = new ArrayList<>();
            try {
                URL url = new URL("http://datafit.com.br/api/task/TaskApi/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ToDoModel task = new ToDoModel();
                        task.setId(jsonObject.getInt("id"));
                        task.setTask(jsonObject.getString("task"));
                        task.setStatus(jsonObject.getInt("status"));
                        tasks.add(task);
                    }
                } else {
                    Log.e("FetchTasksTask", "Server responded with code: " + responseCode);
                }
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tasks;
        }

        @Override
        protected void onPostExecute(List<ToDoModel> tasks) {
            super.onPostExecute(tasks);
            taskList.clear();
            taskList.addAll(tasks);
            tasksAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        fetchTasks();
    }
}
