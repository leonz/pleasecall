package com.bigred.pleasecall;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

class ReminderAdapter extends BaseAdapter {

    private Context context;
    private List<Reminder> reminders;

    public ReminderAdapter(Context context, List<Reminder> values) {
        this.context = context;
        this.reminders = values;
    }

    public void setListData(List<Reminder> values){
    	this.reminders = values;
    }

    @Override
    public int getCount() {
        return reminders.size();
    }

    @Override
    public Object getItem(int position) {
        return reminders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TwoLineListItem twoLineListItem;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, null);
        } else {
            twoLineListItem = (TwoLineListItem) convertView;
        }

        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();

        // Get the contact's name
    	int idx;
    	Cursor cursor = context.getContentResolver().query(Uri.parse(reminders.get(position).getUri()), null, null, null, null);
    	if (cursor.moveToFirst()) {
    	    idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
    	    text1.setText(cursor.getString(idx));
    	}
    	cursor.close();        
        text2.setText("" + reminders.get(position).getDescription());

        return twoLineListItem;
    }
}