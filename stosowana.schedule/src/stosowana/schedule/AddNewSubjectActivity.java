package stosowana.schedule;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddNewSubjectActivity extends FragmentActivity {
	
	private String name,teacher,room;
	int selectedDay;
	private static String startTime = "0:00";
	private static String stopTime = "0:00";
	public static int startHour = 0;
	public static int startMinute = 0;

	public static class SetTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

		

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
		
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			startTime = hourOfDay+":"+minute;
			startHour = hourOfDay;
			startMinute = minute;
			TextView stTxt = (TextView) getActivity().findViewById(R.id.startTimeText);
			stTxt.setText(startTime);
		}
		
		
	}
	public static class StopTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
//			final Calendar c = Calendar.getInstance();
//			int hour = c.get(Calendar.HOUR_OF_DAY);
//			int minute = c.get(Calendar.MINUTE);
			int hour = startHour + 1;
			int minute = startMinute + 30;
			return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			stopTime = hourOfDay+":"+minute;
			TextView stTxt = (TextView) getActivity().findViewById(R.id.stopTimeText);
			stTxt.setText(stopTime);
			
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
		
		DialogFragment newFragment = new SetTimePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "startTimePicker");
	    
	}
	public void setStopTime(View v){
		
		DialogFragment newFragment = new StopTimePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "stopTimePicker");
	}
	public void saveSubject(View view){
		
		Context context = this.getApplicationContext();
		EditText mName = (EditText) findViewById(R.id.addSubject_name);
		EditText mTeacher =(EditText) findViewById(R.id.addSubject_teacher);
		EditText mRoom = (EditText)findViewById(R.id.addSubject_room);
	
		
		name = mName.getText().toString();
		teacher = mTeacher.getText().toString();
		room = mRoom.getText().toString();
//			nie mozna porowynwac == "", nie przynosilo pozadanego efektu
		if(name.isEmpty())
			Toast.makeText(context, "Proszę wpisać nazwę przedmiotu", Toast.LENGTH_SHORT).show();
		else if(teacher.isEmpty())
			Toast.makeText(context, "Proszę wpisać prowadzącego", Toast.LENGTH_SHORT).show();
		else if(room.isEmpty())
			Toast.makeText(context, "Proszę wpisać miejsce", Toast.LENGTH_SHORT).show();
//	czemu nie ma sprawdzenia daty oraz godziny?
		else{
			Subject subject = new Subject();
			subject.setName(name);
			subject.setTeacher(teacher);
			subject.setClassroom(room);
			subject.setStartTime(startTime);
			subject.setStopTime(stopTime);
			subject.setType("2");
			Widget.add(selectedDay, subject);
			finish();
		}
	}
}
