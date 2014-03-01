package stosowana.schedule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewSubjectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_subject_layout);
	}
	
	public void saveSubject(View view){
		Context context = this.getApplicationContext();
		TextView mName = (TextView) findViewById(R.id.addSubject_name);
		TextView mTeacher =(TextView) findViewById(R.id.addSubject_teacher);
		TextView mRoom = (TextView)findViewById(R.id.addSubject_room);
		TextView mStart =(TextView) findViewById(R.id.addSubject_start);
		TextView mStop =(TextView) findViewById(R.id.addSubject_stop);
		TextView mDay =(TextView) findViewById(R.id.addSubject_day);
		
		String name = mName.getText().toString();
		String teacher = mTeacher.getText().toString();
		String room = mRoom.getText().toString();
		String start = mStart.getText().toString();
		String stop = mStop.getText().toString();
		String day = mDay.getText().toString();
		
		if(name.isEmpty())
			Toast.makeText(context, "Proszę wpisać nazwę przedmiotu", Toast.LENGTH_SHORT).show();
		else if(teacher.isEmpty())
			Toast.makeText(context, "Proszę wpisać prowadzącego", Toast.LENGTH_SHORT).show();
		else if(room.isEmpty())
			Toast.makeText(context, "Proszę wpisać miejsce", Toast.LENGTH_SHORT).show();
		else if(start.isEmpty())
			Toast.makeText(context, "Proszę wpisać godzinę rozpoczęcia", Toast.LENGTH_SHORT).show();
		else if(start.charAt(2) != ':' || start.length() !=5)
			Toast.makeText(context, "Zły format daty rozpoczęcia", Toast.LENGTH_SHORT).show();
		else if(stop.isEmpty())
			Toast.makeText(context, "Proszę wpisać godzinę zakończenia", Toast.LENGTH_SHORT).show();
		else if(stop.charAt(2) != ':' || stop.length() !=5)
			Toast.makeText(context, "Zły format daty zakończenia", Toast.LENGTH_SHORT).show();
		else if(day.isEmpty())
			Toast.makeText(context, "Proszę wpisać dzień tygodnia", Toast.LENGTH_SHORT).show();
		else{
			Subject subject = new Subject();
			subject.setName(name);
			subject.setTeacher(teacher);
			subject.setClassroom(room);
			subject.setStartTime(start);
			subject.setStopTime(stop);
			subject.setType("2");
			Widget.add(Integer.parseInt(day), subject);
			finish();
		}
	}
	

}
