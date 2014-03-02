package stosowana.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MenuActivity extends Activity {
	CheckBox cb1 = null;
	CheckBox cb2 = null;
	CheckBox cb3 = null;


	private static final String TAG = "MenuActivity";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
//		Log.d(TAG, Boolean.toString(Widget.box1));
		boolean box1 = Widget.isLectures();
		boolean box2 = Widget.isLaboratories();
		boolean box3 = Widget.isCustom();
		cb1 = (CheckBox) findViewById(R.id.checkBox1);
		cb2 = (CheckBox) findViewById(R.id.checkBox2);
		cb3 = (CheckBox) findViewById(R.id.checkBox3);
		
		if(box1){
			cb1.setChecked(true);	
		}	
		if(box2){
			cb2.setChecked(true);	
		}
		if(box3){
			cb3.setChecked(true);
			
		}
	}
	
	public void fetchData(View view){
		Log.d(TAG, "fetchData");
		Intent i = new Intent(this, DataFetchActivity.class);
		
		Bundle extras = new Bundle();
		extras.putBoolean("LOGIN", true);
		i.putExtras(extras);
		startActivity(i);
		finish();
		
	}
	
	public void updateSieve(View v){
		
		if(cb1 != null && cb2!= null && cb3 != null){
			
			Widget.setLectures(cb1.isChecked());
			Widget.setLaboratories(cb2.isChecked());
			Widget.setCustom(cb3.isChecked());
		}
			
			Log.d("widget", "updating sieve for api 10");
			AppWidgetManager awm = AppWidgetManager.getInstance(getApplicationContext());
			Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, Widget.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,awm.getAppWidgetIds(new ComponentName(getApplicationContext(), Widget.class)));
			sendBroadcast(intent);
		
		finish();
	}
	
	public void addNewSubject(View view){
		Intent i = new Intent(this, AddNewSubjectActivity.class);
		startActivity(i);
	}
}
