package com.davalgi.mytodos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditTasksActivity extends AppCompatActivity {

    private static final String TAG = "EditTasksActivity";
    private final int RESULT_CODE_DELETE = (int) R.integer.RESULT_DELETE;

    private int pos;
    private String editTaskText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tasks);

        editTaskText = getIntent().getExtras().getString("editTaskText");
        pos = getIntent().getExtras().getInt("editTaskPosition");

        EditText etText = (EditText) findViewById(R.id.etTask);
        if (etText != null) {
            etText.setText(editTaskText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent dataIntent = new Intent();

        switch (item.getItemId()) {
            case R.id.action_save_task:

                EditText etText = (EditText) findViewById(R.id.etTask);
                //Toast.makeText(this, etText.getText().toString(), Toast.LENGTH_SHORT).show();

                if (etText != null) {
                    dataIntent.putExtra("editTaskText", etText.getText().toString());
                    dataIntent.putExtra("editTaskPosition", pos);
                }

                setResult(RESULT_OK, dataIntent);

                //Log.d(TAG, "Sending Result code (save): " + RESULT_OK);
                this.finish();
                return true;

            case R.id.action_cancel_task:

                setResult(RESULT_CANCELED);
                //Log.d(TAG, "Sending Result code (cancel): " + RESULT_CANCELED);

                this.finish();
                return true;

            case R.id.action_delete_task:
                dataIntent.putExtra("editTaskText", editTaskText);
                dataIntent.putExtra("editTaskPosition", pos);

                setResult(RESULT_CODE_DELETE, dataIntent);

                //Log.d(TAG, "Sending Result code (delete): " + RESULT_CODE_DELETE);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
