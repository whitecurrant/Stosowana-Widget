package stosowana.schedule;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddNewSubjectActivity extends Activity {
	
	String name,teacher,room,start,stop;
	int selectedDay;

	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_subject_layout);
		Spinner spinner = (Spinner) findViewById(R.id.day_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.days_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				selectedDay = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	public void setStartTime(View v){
		
	}
	public void setStopTime(View v){
		
	}
	public void saveSubject(View view){
		
		Context context = this.getApplicationContext();
		TextView mName = (TextView) findViewById(R.id.addSubject_name);
		TextView mTeacher =(TextView) findViewById(R.id.addSubject_teacher);
		TextView mRoom = (TextView)findViewById(R.id.addSubject_room);
	
		
		name = mName.getText().toString();
		teacher = mTeacher.getText().toString();
		room = mRoom.getText().toString();
		//start = mStart.getText().toString();
		//stop = mStop.getText().toString();
		
		if(name == "")
			Toast.makeText(context, "Proszę wpisać nazwę przedmiotu", Toast.LENGTH_SHORT).show();
		else if(teacher == "")
			Toast.makeText(context, "Proszę wpisać prowadzącego", Toast.LENGTH_SHORT).show();
		else if(room == "")
			Toast.makeText(context, "Proszę wpisać miejsce", Toast.LENGTH_SHORT).show();
	
		else{
			Subject subject = new Subject();
			subject.setName(name);
			subject.setTeacher(teacher);
			subject.setClassroom(room);
			subject.setStartTime(start);
			subject.setStopTime(stop);
			subject.setType("2");
			//Widget.add(Integer.parseInt("1"), subject);
			finish();
		}
	}
}
