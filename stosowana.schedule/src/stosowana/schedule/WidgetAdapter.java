package stosowana.schedule;
import java.util.ArrayList;

import stosowana.schedule.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class WidgetAdapter extends ArrayAdapter<String> {
	    // View lookup cache
	    private static class ViewHolder {
	        TextView first;
	        TextView second;
	    }

	    public WidgetAdapter(Context context, ArrayList<String> items) {
	       super(context, R.layout.row_layout, items);
	       System.out.println("adapting");
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	       // Get the data item for this position
	       String item = getItem(position);    
	       // Check if an existing view is being reused, otherwise inflate the view
	       ViewHolder viewHolder; // view lookup cache stored in tag
	       if (convertView == null) {
	          viewHolder = new ViewHolder();
	          LayoutInflater inflater = LayoutInflater.from(getContext());
	          convertView = inflater.inflate(R.layout.row_layout, null);
	          viewHolder.first = (TextView) convertView.findViewById(R.id.row_time);
	          viewHolder.second = (TextView) convertView.findViewById(R.id.row_label);
	          convertView.setTag(viewHolder);
	       } else {
	           viewHolder = (ViewHolder) convertView.getTag();
	       }
	       // Populate the data into the template view using the data object
	       viewHolder.first.setText(item);
	       viewHolder.second.setText("tatatabababa");
	       // Return the completed view to render on screen
	       System.out.println("working");
	       return convertView;
	   }
}

