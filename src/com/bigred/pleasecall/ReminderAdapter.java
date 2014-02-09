package com.bigred.pleasecall;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

    	View vi = convertView;
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_row, null);
        }

        TextView text1 = (TextView) vi.findViewById(R.id.title);
        TextView text2 = (TextView) vi.findViewById(R.id.subtitle);
        Switch enabled = (Switch) vi.findViewById(R.id.togglebutton);

        // Get the contact's name
    	int idx;
    	Cursor cursor = context.getContentResolver().query(Uri.parse(reminders.get(position).getUri()), null, null, null, null);
    	if (cursor.moveToFirst()) {
    	    idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
    	    text1.setText(cursor.getString(idx));
    	}
    	cursor.close();        
        text2.setText("" + reminders.get(position).getDescription());
        if(reminders.get(position).getEnabled() == 1){
        	enabled.setChecked(true);
        }
        vi.setTag(reminders.get(position).getId());
        
        
        final long id = reminders.get(position).getId();
        enabled.setOnClickListener(new OnClickListener() {
        	 
      	  @Override
      	  public void onClick(View v) {
      		int enabled;
      		if (!((Switch) v).isChecked()) { // Enabled -> Disabled
      			Log.i("checkbox", "enabled->disabled");
      			enabled = 0;
				Toast.makeText(v.getContext(), "Reminder disabled", Toast.LENGTH_SHORT).show();
      		} else { // Disabled -> Enabled
      			Log.i("checkbox", "disabled->enabled");
				Toast.makeText(v.getContext(), "Reminder enabled", Toast.LENGTH_SHORT).show();      			
				enabled = 1;
      		}
      		
			// Save to database
        	ReminderDataSource datasource = new ReminderDataSource(v.getContext());
            datasource.open();	                
            
            // Set this reminder to value of enabled
            Reminder rm = datasource.getReminder(id);
            rm.setEnabled(enabled);
            datasource.editReminder(id, rm.getUri(), rm.getDescription(), rm.getFrequency(), rm.getSMSEnabled(), enabled);
          
            datasource.close();
       
      	  }
      	});
        
        return vi;
    }
}