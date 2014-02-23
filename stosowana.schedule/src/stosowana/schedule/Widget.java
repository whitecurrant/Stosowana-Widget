package stosowana.schedule;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
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

			 //Intent intent = new Intent(context, DataFetchActivity.class);
			 //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 //intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			 //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			 //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
			 //views.setOnClickPendingIntent(R.id.settings_bttn, pendingIntent);
			 //appWidgetManager.updateAppWidget(appWidgetId,views);
		
		
	 	
	} 
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		 
		super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "nieeee!!", Toast.LENGTH_LONG).show();

	}
	
}