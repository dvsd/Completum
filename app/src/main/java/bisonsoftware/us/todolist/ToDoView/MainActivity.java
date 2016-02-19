package bisonsoftware.us.todolist.ToDoView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bisonsoftware.us.todolist.DetailView.DetailItem;
import bisonsoftware.us.todolist.DividerItemDecoration;
import bisonsoftware.us.todolist.R;
import bisonsoftware.us.todolist.SQLiteHelper;

public class MainActivity extends ActionBarActivity {

    SQLiteHelper db = new SQLiteHelper(this, null, null, 1);
    List<ToDoItem> toDoDBList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    MainAdapter mainAdapter;
    private List toDoItems = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoDBList = db.getAllToDos(); //retrieves everything in the database

        //adds every ToDoItem to the RecyclerView on start
        for (int i = 0; i < toDoDBList.size(); i++)
        {
            toDoItems.add(i, new ToDoItem(toDoDBList.get(i).getToDoItemName()));
        }
        //setup everything related to the RecylcerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));
        mainAdapter = new MainAdapter(toDoItems, R.layout.todoitem, this);
        recyclerView.setAdapter(mainAdapter);
        //ItemTouchHelper is used for swipeToDismiss items
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(MainActivity.this, mainAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);

                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        //package a new ArrayList for all the detailItems into a gson string
                                        ArrayList<String> tempArray = new ArrayList<String>();
                                        Gson gson = new Gson();
                                        String inputString= gson.toJson(tempArray);
                                        //add created ToDoItem to the database
                                        db.addProduct(new ToDoItem(userInput.getText().toString()));
                                        toDoDBList = db.getAllToDos();
                                        if (toDoDBList.size() > 0) {
                                            //add created ToDoItem to the RecyclerView
                                            toDoItems.add(toDoDBList.size() - 1, new ToDoItem(toDoDBList.get(toDoDBList.size() - 1).getToDoItemName()));
                                            //create empty ArrayList for DetailItems associated with this ToDoItem to avoid null call when clicked and create column in database
                                            db.addDetailItemName(new DetailItem(inputString, inputString));
                                            mainAdapter.notifyItemInserted(toDoDBList.size() - 1);
                                        }else{
                                            toDoItems.add(0, new ToDoItem(toDoDBList.get(0).getToDoItemName()));
                                            db.addDetailItemName(new DetailItem(inputString, inputString));
                                            mainAdapter.notifyItemInserted(toDoDBList.size());
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                alertDialogBuilder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
