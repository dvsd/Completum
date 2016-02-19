package bisonsoftware.us.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

import bisonsoftware.us.todolist.DetailView.DetailItem;
import bisonsoftware.us.todolist.ToDoView.ToDoItem;

/**
 * Created by devinstafford on 8/17/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "productDB.db";

    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_DETAILS = "details";

    //common columns
    public static final String COLUMN_ID = "_id";
    //todoitem columns
    public static final String COLUMN_PRODUCTNAME = "productname";
    //detailitem columns
    public static final String COLUMN_DETAILID = "detail_id";
    public static final String COLUMN_DETAILNAME = "detailname";
    public static final String COLUMN_CHECKEDSTATE = "checkedstate";

    private static final String TODOTABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCTNAME + " TEXT " +
            ");";
    private static final String DETAILTABLE = "CREATE TABLE " + TABLE_DETAILS + " (" +
            COLUMN_DETAILID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DETAILNAME + " TEXT, " + COLUMN_CHECKEDSTATE + " TEXT " +
            ");";


    //We need to pass database information along to superclass
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TODOTABLE);
        db.execSQL(DETAILTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
        onCreate(db);
    }

    //--------TODOITEM METHODS-------//
    //Add a new row to the database
    public void addProduct(ToDoItem product){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getToDoItemName());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    //Delete a product from the database
    public void deleteProduct(String productName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + "=\"" + productName + "\";");
    }

    public List getAllToDos() {
        List toDoItems = new LinkedList();

        String query = "SELECT * FROM " + TABLE_PRODUCTS;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        ToDoItem toDoItem = null;
        if (cursor.moveToFirst()) {
            do {
                toDoItem = new ToDoItem();
                toDoItem.setId(Integer.parseInt(cursor.getString(0)));
                toDoItem.setToDoItemName(cursor.getString(1));

                toDoItems.add(toDoItem);
            } while (cursor.moveToNext());
        }
        return toDoItems;
    }

    public int updateProduct(ToDoItem toDoItem, String v1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, v1);

        // updating row
        int i = db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[] { String.valueOf(toDoItem.getId()) });

        db.close();
        return i;
    }

    //----------DETAILITEM methods-----------//

    public void addDetailItemName(DetailItem detailItem){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DETAILNAME, detailItem.getDetailText());
        values.put(COLUMN_CHECKEDSTATE, detailItem.getCheckedState());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_DETAILS, null, values);
        db.close();
    }
    public void deleteDetailItem(String detailName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DETAILS + " WHERE " + COLUMN_DETAILNAME + "=\"" + detailName + "\";");
    }

    public List getAllDetailItems() {
        List detailItems = new LinkedList();

        String query = "SELECT * FROM " + TABLE_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        DetailItem detailItem = null;
        if (cursor.moveToFirst()) {
            do {
                detailItem = new DetailItem();
                detailItem.setId(Integer.parseInt(cursor.getString(0)));
                detailItem.setDetailText(cursor.getString(1));
                detailItem.setCheckedState(cursor.getString(2));

                detailItems.add(detailItem);
            } while (cursor.moveToNext());
        }
        return detailItems;
    }

    public int updateDetailItem(DetailItem detailItem, String v1, String v2) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DETAILNAME, v1);
        values.put(COLUMN_CHECKEDSTATE, v2);

        // updating row
        int i = db.update(TABLE_DETAILS, values, COLUMN_DETAILID + " = ?", new String[] { String.valueOf(detailItem.getId()) });

        db.close();
        return i;
    }

}
