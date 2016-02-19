package bisonsoftware.us.todolist.ToDoView;

import java.util.ArrayList;

/**
 * Created by devinstafford on 7/2/15.
 */
public class ToDoItem {
    private String _toDoItemName;
    private int _id;

    public ToDoItem(){}

    public ToDoItem(String toDoItemName){
        this._toDoItemName = toDoItemName;
    }

    public int getId() {
        return _id;
    }
    public void setId(int id) {
        this._id = id;
    }

    public void setToDoItemName(String toDoItemName) {
        this._toDoItemName = toDoItemName;
    }

    public String getToDoItemName(){
        return _toDoItemName;
    }

    @Override
    public String toString() {

        return "ToDoItem [id=" + _id + ", toDoItemName=" + _toDoItemName + "]";

    }

}
