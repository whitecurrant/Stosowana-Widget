package stosowana.schedule;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class AnimHelperService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d("widget", "starting Service");
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.main_widget_layout, null);
		ViewFlipper flipper = (ViewFlipper) view.findViewById(R.id.flipper);
		int direction = intent.getIntExtra("direction", 1);
		if (direction == 1){
			flipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_out));
		}
		else if (direction == -1){
			flipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_out));
		}
		return super.onStartCommand(intent, flags, startId);
	}


	

}
