package stosowana.schedule;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;


@TargetApi(11)
public class RemoteViewsProvider implements RemoteViewsFactory {
	
	private ArrayList<Subject> itemList; 
	private Context context = null;
	private Intent intent; 
	public RemoteViewsProvider(Context context, Intent intent) {
		
		this.context = context;
		this.intent = intent;
	}
	 
	@Override
	public void onCreate() {
		
		Log.d("widget", "onCreate in InnerFactory");
		int dayNum = intent.getIntExtra("dayNum",0);
		itemList = (ArrayList<Subject>) Widget.getSchedule().get(dayNum);
	} 
	@Override
	public int getCount() {
		return itemList.size();
	}
	 
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.row_layout);
		Subject item = itemList.get(position);
		remoteView.setTextViewText(R.id.row_time, item.getStartTime() + " - " + item.getStopTime());
		remoteView.setTextViewText(R.id.row_label, item.toString());
		Log.d("widget", "returning ListView");
		return remoteView;
	}

	@Override
	public RemoteViews getLoadingView() {
		
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.row_layout);
	    remoteView.setTextViewText(R.id.row_time, "");
	    remoteView.setTextViewText(R.id.row_label, "Wczytywanie danych...");
		return remoteView;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onDataSetChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
}