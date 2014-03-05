package stosowana.schedule;

import java.io.File;

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

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MenuActivity extends Activity {

	CheckBox cb1 = null;
	CheckBox cb2 = null;
	CheckBox cb3 = null;

	private static final String TAG = "MenuActivity";

	@Override
	protected void onResume() {
		
		Log.d(TAG, "onResume");
		super.onResume();
		
		DataFetchActivity.loadData(new File(getApplicationContext().getFilesDir().getPath() + "/schedule"));
		if (!(new File(getApplicationContext().getFilesDir().getPath() + "/checkbox/checkbox1").exists()))
			cb1.setChecked(true);
		else
			cb1.setChecked(false);
		if (!(new File(getApplicationContext().getFilesDir().getPath() + "/checkbox/checkbox2").exists()))
			cb2.setChecked(true);
		else
			cb2.setChecked(false);
		if (!(new File(getApplicationContext().getFilesDir().getPath() + "/checkbox/checkbox3").exists()))
			cb3.setChecked(true);
		else
			cb3.setChecked(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		// Log.d(TAG, Boolean.toString(Widget.box1));

		cb1 = (CheckBox) findViewById(R.id.checkBox1);
		cb2 = (CheckBox) findViewById(R.id.checkBox2);
		cb3 = (CheckBox) findViewById(R.id.checkBox3);
	}

	public void fetchData(View view) {

		Log.d(TAG, "fetchData");
		Intent i = new Intent(this, DataFetchActivity.class);

		Bundle extras = new Bundle();
		extras.putBoolean("LOGIN", true);
		i.putExtras(extras);
		startActivity(i);
		finish();
	}

	public void updateSieve(View v) {

		if (cb1 != null && cb2 != null && cb3 != null) {
			Log.d(TAG, "not null!");
			if (Widget.getSchedule() == null)
				Log.d(TAG, "schedule null");
			Widget.setLectures(cb1.isChecked());
			Widget.setLaboratories(cb2.isChecked());
			Widget.setCustom(cb3.isChecked());

			File file = new File(getApplicationContext().getFilesDir().getPath() + "/checkbox");
			if (file.exists())
				DataFetchActivity.deleteScheduleDir(file);
			file.mkdir();
			if (!cb1.isChecked()) {
				File checkbox1 = new File(file.getPath() + "/checkbox1");
				checkbox1.mkdir();
			}
			if (!cb2.isChecked()) {
				File checkbox2 = new File(file.getPath() + "/checkbox2");
				checkbox2.mkdir();
			}
			if (!cb3.isChecked()) {
				File checkbox3 = new File(file.getPath() + "/checkbox3");
				checkbox3.mkdir();
			}
		}

		Log.d("widget", "updating sieve for api 10");
		AppWidgetManager awm = AppWidgetManager.getInstance(getApplicationContext());
		Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, Widget.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
				awm.getAppWidgetIds(new ComponentName(getApplicationContext(), Widget.class)));
		sendBroadcast(intent);

		finish();
	}

	public void addNewSubject(View view) {
		Intent i = new Intent(this, AddNewSubjectActivity.class);
		startActivity(i);
	}
}
