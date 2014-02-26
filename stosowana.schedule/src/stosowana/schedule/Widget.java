package stosowana.schedule;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Widget extends AppWidgetProvider {

	private Context context;
	private static boolean isEmpty = true;
	public static final String SHOW_NEXT = "stosowana.schedule.SHOW_NEXT";
	public static final String SHOW_PREV = "stosowana.schedule.SHOW_PREV";
	private static final String TAG="widget";
	private static Map<Integer, List<Subject> > schedule;
	public static int dayNum = 0;
	
	private static boolean lectures = true;
	private static boolean laboratories = true;
	private static boolean excercise = true;
	
	
	
	/**
	 * Przy okazji update'u widgetu sprawdzamy czy nie jest pusty (jeśli tak to ustawiamy pusty widok) a po sprawdzeniu 
	 * wersji wywołujemy odpowiednią funkcję updatującą dla każdej instancji widgetu (ktoś mógł ich dodać kilka) 
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager awm,int[] appWidgetIds) {

		this.context = context;
		
		Log.d(TAG, "updating...");

		if (isEmpty){
//			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
//			for (int widgetID : appWidgetIds)
//				awm.updateAppWidget(widgetID, rv);
			return;			
		}
		
		List<Subject> dayList = schedule.get(dayNum); 
		Collections.sort(dayList);
		Log.d(TAG, "day nr " + dayNum);
		Log.d(TAG, dayList.toString());
		
		if (Build.VERSION.SDK_INT >= 11) {
			Log.d(TAG, "for api 11");
			for (int widgetID : appWidgetIds)
				updateWidgetAPI11(awm, widgetID);
		} else {
			Log.d(TAG, "for api 10");
			for (int widgetID : appWidgetIds)
				updateWidgetAPI10(awm, widgetID, dayList);
		}
		super.onUpdate(context, awm, appWidgetIds);
	}
	private void updateCurrentDay(){
		
		Calendar c = Calendar.getInstance();
		int i = c.get(Calendar.DAY_OF_WEEK); // co dzisiaj mamy?
		dayNum = i - 2; // niedziela = 1
	}
	
	/**
	 * Dla starszych wersji ręcznie tworzymy widok 'listy' i wypełniamy pola dla każdego przedmiotu
	 */
	private void updateWidgetAPI10(AppWidgetManager awm, int widgetID, List<Subject> dayList) {

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
		//usuwamy poprzednie widoki
		views.removeAllViews(R.id.container);
		// ustawiamy intenty opakowane w pending intenty i przypisujemy do przycisków
		Intent intentNext = new Intent(context,Widget.class);
		Intent intentPrev = new Intent(context,Widget.class);
		Intent menu = new Intent(context,MenuActivity.class);
		intentNext.setAction(SHOW_NEXT);
		intentPrev.setAction(SHOW_PREV);
//		menu.setAction(MENU);
		PendingIntent pIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, 0);
		PendingIntent pIntentPrev = PendingIntent.getBroadcast(context, 0, intentPrev, 0);
		PendingIntent pMenu = PendingIntent.getActivity(context, 0, menu, 0);
		views.setOnClickPendingIntent(R.id.right_arrow_btn, pIntentNext);
		views.setOnClickPendingIntent(R.id.left_arrow_btn, pIntentPrev);
		views.setOnClickPendingIntent(R.id.settings_bttn, pMenu);
		//wypełniamy widoki listy
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
		
		if(intent.getAction().equals(SHOW_NEXT)){
			
			Log.d(TAG, "showing next");
			dayNum = (++dayNum)%5;
			//zeby wymusic funkcję onUpdate z super trzeba zmodyfikowac intent
			intent  = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, context, Widget.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, Widget.class)));
		}
		else if (intent.getAction().equals(SHOW_PREV)){
			
			Log.d(TAG, "showing previous");
			dayNum = (--dayNum+5)%5;
			intent  = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, context, Widget.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, Widget.class)));
		}
		
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
	public static boolean isLectures() {
		return lectures;
	}
	public static void setLectures(boolean lectures) {
		Widget.lectures = lectures;
	}
	public static boolean isLaboratories() {
		return laboratories;
	}
	public static void setLaboratories(boolean laboratories) {
		Widget.laboratories = laboratories;
	}
	public static boolean isExcercise() {
		return excercise;
	}
	public static void setExcercise(boolean excercise) {
		Widget.excercise = excercise;
	}
}