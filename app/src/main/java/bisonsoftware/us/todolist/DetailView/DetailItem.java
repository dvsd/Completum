package bisonsoftware.us.todolist.DetailView;

import java.util.ArrayList;

/**
 * Created by devinstafford on 12/5/15.
 */
public class DetailItem {
    public String _detailText;
    private int _id;
    private String _checkedState;

    public DetailItem(){}

    public int getId() {
        return _id;
    }
    public void setId(int id) {
        this._id = id;
    }

    public DetailItem(String detailText, String checkedState){
        this._detailText = detailText;
        this._checkedState = checkedState;
    }

    public String getDetailText() {
        return _detailText;
    }

    public void setDetailText(String detailText) {
        this._detailText = detailText;
    }

    public String getCheckedState(){
        return _checkedState;
    }

    public void setCheckedState(String checkedState) {
        this._checkedState = checkedState;
    }

    @Override
    public String toString() {

        return "DetailItem [id=" + _id + ", detailItemName=" + _detailText + ", detailCheckedState=" + _checkedState + "]";

    }
}
