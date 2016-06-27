package com.davalgi.mytodos;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ArrayList<String> tasks;
    ArrayAdapter<String> tasksAdapter;
    ListView lvTasks;

    private final int REQUEST_CODE_EDIT = 200;
    private final int RESULT_CODE_DELETE = (int) R.integer.RESULT_DELETE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTasks = (ListView) findViewById(R.id.lvTask);
        tasks = new ArrayList<String>();
        readTasks();
        tasksAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);
        lvTasks.setAdapter(tasksAdapter);
        setupListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);

                AlertDialog addTaskDialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_add_task_title)
                        .setMessage(R.string.dialog_add_task_text)
                        .setView(taskEditText)
                        .setPositiveButton(R.string.add_task, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                //Log.d(TAG, "Task to add: " + task);
                                tasksAdapter.add(task);
                                Toast.makeText(getApplicationContext(), task + " task added", Toast.LENGTH_SHORT).show();
                                writeTasks();
                            }
                        })
                        .setNegativeButton(R.string.cancel_task, null)
                        .create();
                addTaskDialog.show();
                writeTasks();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupListViewListener(){
        /*
        lvTasks.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick (AdapterView<?> adapter, View item, int pos, long id){
                        String removeTaskText = adapter.getItemAtPosition(pos).toString();
                        tasks.remove(pos);
                        Toast.makeText(getApplicationContext(), removeTaskText + " removed", Toast.LENGTH_SHORT).show();
                        tasksAdapter.notifyDataSetChanged();
                        writeTasks();
                        return true;
                    }
                }
        );

        lvTasks.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick (AdapterView<?> adapter, View item, int pos, long id){
                        Intent editTaskIntent = new Intent(getApplicationContext(), EditTasksActivity.class);

                        String editTaskText = adapter.getItemAtPosition(pos).toString();

                        editTaskIntent.putExtra("editTaskText", editTaskText);
                        editTaskIntent.putExtra("editTaskPosition", pos);

                        startActivityForResult(editTaskIntent, REQUEST_CODE_EDIT);
                    }
                }
        );
        */

        lvTasks.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick (AdapterView<?> adapter, View item, int pos, long id){
                        Intent editTaskIntent = new Intent(getApplicationContext(), EditTasksActivity.class);

                        String editTaskText = adapter.getItemAtPosition(pos).toString();

                        editTaskIntent.putExtra("editTaskText", editTaskText);
                        editTaskIntent.putExtra("editTaskPosition", pos);

                        startActivityForResult(editTaskIntent, REQUEST_CODE_EDIT);

                        return true;
                    }
                }
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //Log.d(TAG, "Receiving Result code: " + resultCode);

        int editTaskPos;
        String editTaskText;

        if (requestCode == REQUEST_CODE_EDIT) {
            switch (resultCode) {
                case RESULT_OK:
                    editTaskPos = data.getExtras().getInt("editTaskPosition");
                    editTaskText = data.getExtras().getString("editTaskText");

                    tasks.remove(editTaskPos);
                    tasks.add(editTaskPos, editTaskText);

                    Toast.makeText(getApplicationContext(), "Task edit Saved", Toast.LENGTH_SHORT).show();

                    tasksAdapter.notifyDataSetChanged();
                    writeTasks();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(), "Task edit Canceled", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CODE_DELETE:
                    editTaskPos = data.getExtras().getInt("editTaskPosition");
                    //editTaskText = data.getExtras().getString("editItemText");

                    tasks.remove(editTaskPos);
                    tasksAdapter.notifyDataSetChanged();
                    writeTasks();

                    Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }

        }
    }

    private void readTasks() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "mytodos.txt");
        try{
            tasks = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            tasks = new ArrayList<String>();
        }
    }

    private void writeTasks(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "mytodos.txt");
        try{
            FileUtils.writeLines(todoFile, tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
