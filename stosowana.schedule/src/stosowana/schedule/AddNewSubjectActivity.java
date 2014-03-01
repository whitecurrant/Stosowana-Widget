package stosowana.schedule;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AddNewSubjectActivity extends FragmentActivity {
	
	private String name,teacher,room,startT,stopT;
	int selectedDay;
	private static String startTime = "0:00";
	private static String stopTime = "0:00";

	public static class SetTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

		
		static final Calendar c = Calendar.getInstance();
		static int hour = c.get(Calendar.HOUR_OF_DAY);
		static int minute = c.get(Calendar.MINUTE);
		public static int chosenHour,chosenMinute;
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			startTime = ((hourOfDay <10) ? "0" + hourOfDay : hourOfDay )+ ":"+ ((minute < 10) ? "0" + minute : minute) ;
			TextView stTxt = (TextView) getActivity().findViewById(R.id.startTimeText);
			stTxt.setText(startTime);
			chosenHour = hourOfDay;
			chosenMinute = minute;
		}
	}
	public static class StopTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			int minute = SetTimePickerFragment.chosenMinute + 30;
			int hour = SetTimePickerFragment.chosenHour + 1 ;
			if(minute > 60)
			{
				minute -=60;
				hour++;
			}	
			if(hour >= 24)
				hour -=24;

			return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			stopTime = ((hourOfDay <10) ? "0" + hourOfDay  : hourOfDay )+ ":"+ ((minute < 10) ? "0" + minute : minute) ;
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
		TextView startText = (TextView)findViewById(R.id.startTimeText);
		TextView stopText = (TextView)findViewById(R.id.stopTimeText);
	
		
		name = mName.getText().toString();
		teacher = mTeacher.getText().toString();
		room = mRoom.getText().toString();
		startT = startText.toString();
		stopT = startText.toString();

		if(name.isEmpty())
			Toast.makeText(context, "Proszę wpisać nazwę przedmiotu", Toast.LENGTH_SHORT).show();
		else if(teacher.isEmpty())
			Toast.makeText(context, "Proszę wpisać prowadzącego", Toast.LENGTH_SHORT).show();
		else if(room.isEmpty())
			Toast.makeText(context, "Proszę wpisać miejsce", Toast.LENGTH_SHORT).show();
		else if(startT.isEmpty())
			Toast.makeText(context, "Proszę wybrać godzinę zajęć", Toast.LENGTH_SHORT).show();
		else if(stopT.isEmpty())	
			Toast.makeText(context, "Proszę wybrać godzinę zajęć", Toast.LENGTH_SHORT).show();
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
