package stosowana.schedule;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
 
public class Widget extends AppWidgetProvider {
 
	//komentarz
	 	
	 @Override
     public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
   
		 final int N = appWidgetIds.length;
		 for (int i=0; i<N; i++) {
			 
		 
			 int appWidgetId = appWidgetIds[i];
			 Intent intent = new Intent(context, DataFetchActivity.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 // Make the pending intent unique...
			 intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			 PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
			 RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
			 views.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);
			 appWidgetManager.updateAppWidget(appWidgetId,views);
	 	}
     }
	
}