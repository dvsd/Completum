package bisonsoftware.us.todolist.DetailView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import bisonsoftware.us.todolist.DividerItemDecoration;
import bisonsoftware.us.todolist.R;
import bisonsoftware.us.todolist.SQLiteHelper;
import bisonsoftware.us.todolist.ToDoView.*;

public class DetailActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    DetailAdapter detailAdapter;
    private List detailItems = new ArrayList();
    SQLiteHelper db = new SQLiteHelper(this, null, null, 1);
    List<DetailItem> detailItemDBList;
    ArrayList<String> tempArray = new ArrayList<String>();
    ArrayList<String> checkedTempArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //receive the position of the clicked ToDoItem
        Bundle extras = getIntent().getExtras();
        final int pastPosition = extras.getInt("Position");

        //retrieve all DetailItems(full of GSON strings)
        detailItemDBList = db.getAllDetailItems();
        //parse the tasks and checkedStates into two arrays
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList>() {}.getType();
        tempArray = gson.fromJson(detailItemDBList.get(pastPosition).getDetailText(), type);
        checkedTempArray = gson.fromJson(detailItemDBList.get(pastPosition).getCheckedState(), type);

        //add each detailItem(comprised of task and checkedState) to recyclerview
        for (int i = 0; i < tempArray.size(); i++) {
            detailItems.add(i, new DetailItem(tempArray.get(i), checkedTempArray.get(i)));
        }

        //setup recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));
        detailAdapter = new DetailAdapter(detailItems, R.layout.detail_item, this, pastPosition);
        recyclerView.setAdapter(detailAdapter);
        //ItemToucheHelper used for swipeToDismiss items
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(DetailActivity.this, detailAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(DetailActivity.this);
                View promptsView = li.inflate(R.layout.prompts2, null);

                //build AlertDialog on FAB clicked
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        DetailActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        //add new ToDoString to the tempArray
                                        tempArray.add(userInput.getText().toString());
                                        Gson gson = new Gson();
                                        //package the new tempArray into a GSON string
                                        String inputString= gson.toJson(tempArray);
                                        //add "0" value to checkedStateArray whenever a new task is created
                                        if (checkedTempArray.size() > 0) {
                                            checkedTempArray.add(checkedTempArray.size()-1, "0");
                                        }else{
                                            checkedTempArray.add(0, "0");
                                        }
                                        //package the new checkedTempArray into a GSON string
                                        String checkedInputString = gson.toJson(checkedTempArray);
                                        //updateDetailItem to insert new task tempArray and checkedTempArray into the DB
                                        db.updateDetailItem(detailItemDBList.get(pastPosition), inputString, checkedInputString);
                                        detailItemDBList = db.getAllDetailItems();
                                        //add DetailItem to RecyclerView
                                        if (detailItems.size() > 0) {
                                            detailItems.add(tempArray.size()-1, new DetailItem(tempArray.get(tempArray.size()-1), checkedTempArray.get(checkedTempArray.size()-1)));
                                            detailAdapter.notifyItemInserted(tempArray.size()-1);
                                        }else{
                                            detailItems.add(0, new DetailItem(tempArray.get(0), checkedTempArray.get(0)));
                                            detailAdapter.notifyItemInserted(0);
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
}
