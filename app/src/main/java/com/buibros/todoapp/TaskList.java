package com.buibros.todoapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TaskList extends AppCompatActivity {
    private TaskDBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        updateUI();
    }

    private void updateUI() {
        helper = new TaskDBHelper(TaskList.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null, null, null, null, null);

        ListAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item_task,
                cursor,
                new String[] { TaskContract.Columns.TASK},
                new int[] { R.id.taskTextView},
                0
        );
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_add_task) {
            Intent newTaskIntent = new Intent(this, NewTaskInput.class);
            startActivity(newTaskIntent);

            return true;
        }



            return super.onOptionsItemSelected(item);
    }
    public void onDoneButtonClick(View view)
    {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
        TaskContract.TABLE,
        TaskContract.Columns.TASK,
        task);
        helper = new TaskDBHelper(this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }
}
