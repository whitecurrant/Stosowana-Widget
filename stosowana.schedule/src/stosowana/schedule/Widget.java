package stosowana.schedule;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;
 
public class Widget extends AppWidgetProvider {
 
		
	Context context;
	 
	 @Override
     public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
   
		 this.context = context;
		 
		 for(int widgetID:appWidgetIds){
			 updateWidget(appWidgetManager, widgetID);
		 }
     }
	 
	private void updateWidget(AppWidgetManager awm, int widgetID){

	 	
	} 
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		String action = intent.getAction();
	    if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action))
	    {
	        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
	        AppWidgetManager.getInstance(context).updateAppWidget(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);
	    }
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		 
		super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "nieeee!!", Toast.LENGTH_LONG).show();

	}
	
}