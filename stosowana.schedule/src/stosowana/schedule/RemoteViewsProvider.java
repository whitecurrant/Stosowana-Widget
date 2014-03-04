package stosowana.schedule;

import java.util.ArrayList;

import android.annotation.TargetApi;
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

	}

	@Override
	public int getCount() {

		if (itemList == null)
			return 0;
		return itemList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getViewAt(int position) {

		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.row_layout);

		if (position < getCount()) {
			Subject item = itemList.get(position);
			remoteView.setTextViewText(R.id.row_time, item.getStartTime() + " - " + item.getStopTime());
			remoteView.setTextViewText(R.id.row_type, item.getType().toString());
			remoteView.setTextViewText(R.id.row_label, item.toString());
		} else
			Log.d("widget", "returning empty view");
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
		return false;
	}

	@Override
	public void onDataSetChanged() {

		int dayNum = intent.getIntExtra("dayNum", 0);
		if (Widget.getSchedule() == null) {
			Log.d("widget", "list Empty in OndataSetChanged for day  " + intent.getIntExtra("dayNum", -1));
			return;
		}
		itemList = (ArrayList<Subject>) Widget.subjectSieve(Widget.getSchedule().get(dayNum));
	}

	@Override
	public void onDestroy() {
	}
}
