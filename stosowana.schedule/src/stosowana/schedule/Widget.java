package stosowana.schedule;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Widget extends AppWidgetProvider {

	Context context;
	private static boolean isEmpty = true;
	private static Map<Integer, List<Subject> > schedule;
	public static int dayNum = 0;
	

	/**
	 * Przy okazji update'u widgetu sprawdzamy czy nie jest pusty (jeśli tak to ustawiamy pusty widok) i po sprawdzeniu 
	 * wersji wywołujemy odpowiednią funkcję updatującą dla każdej instancji widgetu (ktoś mógł ich dodać kilka)
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager awm,int[] appWidgetIds) {

		this.context = context;
		
		Log.d("widget", "updating");

		if (isEmpty){
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
			for (int widgetID : appWidgetIds)
				awm.updateAppWidget(widgetID, rv);
			return;			
		}
		
		Calendar c = Calendar.getInstance();
		int i = c.get(Calendar.DAY_OF_WEEK); // co dzisiaj mamy?
		List<Subject> dayList = schedule.get(i - 2); // niedziela = 1																	// = 1
		Collections.sort(dayList);
		
		if (Build.VERSION.SDK_INT >= 11) {
			for (int widgetID : appWidgetIds)
				updateWidgetAPI11(awm, widgetID);
		} else {
			for (int widgetID : appWidgetIds)
				updateWidgetAPI10(awm, widgetID, dayList);
		}
		super.onUpdate(context, awm, appWidgetIds);
	}
	/**
	 * Dla starszych wersji ręcznie tworzymy widok 'listy' i wypełniamy pola dla każdego przedmiotu
	 */
	private void updateWidgetAPI10(AppWidgetManager awm, int widgetID, List<Subject> dayList) {

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);

		for (Subject sub : dayList) {

			RemoteViews innerView = new RemoteViews(context.getPackageName(), R.layout.row_layout);
			innerView.setTextViewText(R.id.row_time, sub.getStartTime() + " - " + sub.getStopTime());
			innerView.setTextViewText(R.id.row_label, sub.toString());
			views.addView(R.id.container, innerView);
		}
		awm.updateAppWidget(widgetID, views);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "sam się usuń!!", Toast.LENGTH_LONG).show();

	}
	/**
	 * Wypełnienie widoku dla nowszych wersji poprzez ustawienie adaptera (RemoteViewsProvider to obudowany adapter)
	 * dla ListView  za pomocą WidgetService. Adapter pobiera dane, tworzy i zwraca widoki (RemoteViews) automatycznie
	 * pobierane przy wypełnianiu listy.
	 */
	@TargetApi(11)
	@SuppressWarnings("deprecation")
	private void updateWidgetAPI11(AppWidgetManager awm, int appWidgetId) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
		awm.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView);
		Intent serviceIntent = new Intent(context, WidgetService.class);
		serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
		remoteViews.setRemoteAdapter(appWidgetId, R.id.listView, serviceIntent);
		remoteViews.setEmptyView(R.id.listView, R.id.empty_view);
		awm.updateAppWidget(appWidgetId, remoteViews);
	}
	public static void setSchedule(Map<Integer, List<Subject> > schedule){
		Widget.schedule = schedule;
		isEmpty = false;
	}
	public static Map<Integer, List<Subject> > getSchedule(){
		return Widget.schedule;
	}
}