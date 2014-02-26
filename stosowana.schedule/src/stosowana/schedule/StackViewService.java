package stosowana.schedule;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;

@TargetApi(11)
public class StackViewService extends RemoteViewsService {
	
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
    	
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class StackRemoteViewsFactory implements RemoteViewsFactory {

	
	private TestData data;
	private ArrayList<Subject> itemList; 
	private Context context = null;
	private int appWidgetId;
	
	public StackRemoteViewsFactory(Context context,Intent intent){
	
		this.context = context;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
	}
	@Override
	public void onCreate() {
		
		data =  new TestData();
		itemList = (ArrayList<Subject>) Widget.getSchedule().get(Widget.dayNum);
		Log.d("widget", "onCreate in a factory");
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
	public RemoteViews getLoadingView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {

		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.day_layout);
		Intent serviceIntent = new Intent(context, WidgetService.class);
		serviceIntent.putExtra("dayNum", position);
		serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
		remoteView.setRemoteAdapter(appWidgetId, R.id.listView, serviceIntent);
		remoteView.setEmptyView(R.id.listView, R.id.empty_listView);
	
		return remoteView;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
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

//... include adapter-like methods here. See the StackView Widget sample.

}