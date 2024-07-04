package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.ToDoAdapter;
import com.example.myapplication.model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private List<ToDoModel> taskList;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

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
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<ToDoModel> tasks = new ArrayList<>();
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

                        JSONObject responseObject = new JSONObject(response.toString());
                        JSONArray jsonArray = responseObject.getJSONArray("results");

                        Log.d("FetchTasksTask", "Response Object: " + responseObject.toString());
                        Log.d("FetchTasksTask", "JSON Array: " + jsonArray.toString());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ToDoModel task = new ToDoModel();
                            task.setIdTask(jsonObject.getInt("id_task"));
                            task.setTask(jsonObject.getString("task"));
                            task.setTaskStatus(jsonObject.getInt("taskStatus"));
                            tasks.add(task);
                        }
                    } else {
                        Log.e("FetchTasksTask", "Erro ao buscar as tarefas " + responseCode);
                    }
                    urlConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        taskList.clear();
                        taskList.addAll(tasks);
                        tasksAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        fetchTasks();
    }
}
