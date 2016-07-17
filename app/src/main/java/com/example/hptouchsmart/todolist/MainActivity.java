package com.example.hptouchsmart.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hptouchsmart.todolist.Models.Task;
import com.example.hptouchsmart.todolist.db.TaskTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addClass;
    EditText etTask;
    static SQLiteDatabase db;
    static ArrayAdapter arrayAdapter;
    ListView listView;
    static ArrayList<Task> list = new ArrayList<>();
    static databaseAdapter adapter;
    Button button_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addClass = (Button) findViewById(R.id.button_add_task);
        etTask = (EditText) findViewById(R.id.et_todo);
        listView = (ListView) findViewById(R.id.toDoList);
        button_delete = (Button) findViewById(R.id.button_cancel);

        //arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,list );





        db = DbOpener.openWritableData(MainActivity.this);
        adapter = new databaseAdapter(list);
        tasksInDatabase();
        listView.setAdapter(adapter);
        // this is archit 



        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String taskString = etTask.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etTask.getWindowToken(),0);

                if (!taskString.isEmpty()) {


                    etTask.setText("");
                    long date = System.currentTimeMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
                    String dateString = sdf.format(date);

                    Task task = new Task(taskString, 0,dateString);


                    ContentValues contentValues = new ContentValues();



                    contentValues.put(TaskTable.Colums.TASK, task.getTask());
                    contentValues.put(TaskTable.Colums.DONE, task.getDone());
                    contentValues.put(TaskTable.Colums.DATE, task.getDate());


                    db.insert(TaskTable.TABLE_NAME, null, contentValues);

                    tasksInDatabase();

                    Toast.makeText(MainActivity.this, "task Saved !!", Toast.LENGTH_SHORT).show();
                    //db.close();
                }else{
                    Toast.makeText(MainActivity.this, "No Task Entered !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView tvClicked = (TextView) view.findViewById(R.id.positionTV);
                String stringclicked = tvClicked.getText().toString();

                int clickedId = Integer.parseInt(stringclicked);

                UpdateTask(clickedId);
                //updateDatabase(clickedId , position);
                tasksInDatabase();
                changeListTuple(clickedId, view);

            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteRows();
            }
        });


    }

    public void DeleteRows(){
        String selection = TaskTable.Colums.DONE + " LIKE ?";
        String[] selectionArgs = {"1"};
        db.delete(TaskTable.TABLE_NAME, selection, selectionArgs);
        tasksInDatabase();
     }


    public Cursor GetFromDatabase(int id) {
        String[] projection = {
                TaskTable.Colums.ID,
                TaskTable.Colums.TASK,
                TaskTable.Colums.DONE,
                TaskTable.Colums.DATE

        };

        String selection = TaskTable.Colums.ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        if (db != null) {
            Cursor c = db.query(
                    TaskTable.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            return c;
        }else {
            return null;
        }
    }



    public void UpdateTask(int id) {

      //  db = DbOpener.openWritableData(MainActivity.this);

            Cursor c = GetFromDatabase(id);
            c.moveToNext();
            int done = c.getInt(c.getColumnIndexOrThrow(TaskTable.Colums.DONE));

            //Task thisTask = list.get(position);

            if(done == 0){
                done = 1;
                Log.d("TAG","Done = 1");
            }else if(done == 1){
                done = 0;
                Log.d("TAG", "Done = 0");
            }

        ContentValues values = new ContentValues();
        values.put(TaskTable.Colums.DONE,done);
        String selection = TaskTable.Colums.ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        int count = db.update(
                TaskTable.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
            //thisTask.setDone(done);

           // db.close();

        }



    public void changeListTuple(int id, View view){

        Log.d("TAG","view changes called");
        TextView tuple = (TextView) view.findViewById(R.id.tv_task);

        Cursor c = GetFromDatabase(id);
        c.moveToNext();

        int done = c.getInt(c.getColumnIndex(TaskTable.Colums.DONE));

        if(done == 1){
            tuple.setPaintFlags(tuple.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else if(done == 0){
            tuple.setPaintFlags(tuple.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }


    }



    public void tasksInDatabase(){

        ArrayList<String> tasks = new ArrayList<>();
        list.clear();

       // db = DbOpener.openWritableData(MainActivity.this);
        String[] projection  = {
                TaskTable.Colums.ID,
                TaskTable.Colums.TASK,
                TaskTable.Colums.DONE,
                TaskTable.Colums.DATE
        };

        if( db != null) {
            Cursor c = db.query(
              TaskTable.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            while(c.moveToNext()){
                int id = c.getInt(c.getColumnIndex(TaskTable.Colums.ID));
                String task = c.getString(c.getColumnIndex(TaskTable.Colums.TASK));
                int done = c.getInt(c.getColumnIndex(TaskTable.Colums.DONE));
                String date = c.getString(c.getColumnIndex(TaskTable.Colums.DATE));
                Log.d("TAG","ID:"+ id+ " " + task + " " + done);
                Task thisTask = new Task(id,task,done,date);
                list.add(thisTask);
            }
            adapter.notifyDataSetChanged();

            //db.close();

        }
    }





    public class databaseAdapter extends BaseAdapter{

        ArrayList<Task> task;

        public  class ViewHolder{
            TextView tvList;
        }



        public databaseAdapter(ArrayList<Task> task) {
            this.task = task;
        }

        @Override
        public int getCount() {

            return task.size();
        }

        @Override
        public Task getItem(int position) {
              return task.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater li = getLayoutInflater();
            ViewHolder viewHolder;

            if(convertView == null){
                convertView = li.inflate(R.layout.each_tuple,null);
                viewHolder = new ViewHolder();
                viewHolder.tvList = (TextView) convertView.findViewById(R.id.tv_task);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TextView tv = (TextView) convertView.findViewById(R.id.tv_task);
            TextView tvPosition = (TextView) convertView.findViewById(R.id.positionTV);
            TextView date = (TextView) convertView.findViewById(R.id.date_tv);

            Task thiTask = getItem(position);

            int done = thiTask.getDone();
            tv.setText(thiTask.getTask());
            tvPosition.setText(Integer.toString(thiTask.getId()));
            date.setText(thiTask.getDate());

            if(done == 1){
                tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else if(done == 0){
                tv.setPaintFlags(tv.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }


            return convertView;
        }

    }



}
