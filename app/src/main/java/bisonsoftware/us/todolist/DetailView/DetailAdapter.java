package bisonsoftware.us.todolist.DetailView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import bisonsoftware.us.todolist.R;
import bisonsoftware.us.todolist.SQLiteHelper;
import bisonsoftware.us.todolist.ToDoView.ToDoItem;

/**
 * Created by devinstafford on 12/5/15.
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private int rowLayout;
    private Context mContext;
    RecyclerView.Adapter detailAdapter;
    private List<DetailItem> detailItems;
    List<DetailItem> detailItemDBList;
    private int pastPosition;
    ArrayList<String> tempArray;
    ArrayList<String> checkedTempArray;

    public DetailAdapter(List<DetailItem> detailItems, int rowLayout, Context context, int pastPosition) {

        this.detailItems = detailItems;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.pastPosition = pastPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final DetailItem detailItem = detailItems.get(position);
        viewHolder.detailText.setText(detailItem.getDetailText());

        SQLiteHelper db = new SQLiteHelper(mContext, null, null, 1);
        detailItemDBList = db.getAllDetailItems();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList>() {}.getType();
        checkedTempArray = gson.fromJson(detailItemDBList.get(pastPosition).getCheckedState(), type);
        if (checkedTempArray.get(position).equals("0")){
            viewHolder.checkBox.setChecked(false);
        }else{
            viewHolder.checkBox.setChecked(true);
        }
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()){
                    SQLiteHelper db = new SQLiteHelper(mContext, null, null, 1);
                    detailItemDBList = db.getAllDetailItems();
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList>() {}.getType();
                    checkedTempArray = gson.fromJson(detailItemDBList.get(pastPosition).getCheckedState(), type);
                    checkedTempArray.set(position, "1");
                    String checkedInputString = gson.toJson(checkedTempArray);
                    db.updateDetailItem(detailItemDBList.get(pastPosition), detailItemDBList.get(pastPosition).getDetailText(), checkedInputString);
                }else{
                    SQLiteHelper db = new SQLiteHelper(mContext, null, null, 1);
                    detailItemDBList = db.getAllDetailItems();
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList>() {}.getType();
                    checkedTempArray = gson.fromJson(detailItemDBList.get(pastPosition).getCheckedState(), type);
                    checkedTempArray.set(position, "0");
                    String checkedInputString = gson.toJson(checkedTempArray);
                    db.updateDetailItem(detailItemDBList.get(pastPosition), detailItemDBList.get(pastPosition).getDetailText(), checkedInputString);
                }
            }
        });
    }

    public void onItemDismiss(int position){
        SQLiteHelper db = new SQLiteHelper(mContext, null, null, 1);
        detailItemDBList = db.getAllDetailItems();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList>() {}.getType();
        tempArray = gson.fromJson(detailItemDBList.get(pastPosition).getDetailText(), type);
        tempArray.remove(position);
        checkedTempArray = gson.fromJson(detailItemDBList.get(pastPosition).getCheckedState(), type);
        checkedTempArray.remove(position);
        String inputString= gson.toJson(tempArray);
        String checkedInputString = gson.toJson(checkedTempArray);
        db.updateDetailItem(detailItemDBList.get(pastPosition), inputString, checkedInputString);
        detailItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return detailItems == null ? 0 : detailItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView detailText;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            detailText = (TextView) itemView.findViewById(R.id.detailText);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);

        }
    }
}
