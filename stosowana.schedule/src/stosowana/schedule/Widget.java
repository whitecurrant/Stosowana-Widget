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
	
	private RemoteViews mRemoteViews;
	
	
	
	/**
	 * Przy okazji update'u widgetu sprawdzamy czy nie jest pusty (jeśli tak to ustawiamy pusty widok) a po sprawdzeniu 
	 * wersji wywołujemy odpowiednią funkcję updatującą dla każdej instancji widgetu (ktoś mógł ich dodać kilka) 
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager awm,int[] appWidgetIds) {

		this.context = context;
		
		Log.d(TAG, "updating...");

		if (isEmpty){
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
			for (int widgetID : appWidgetIds)
				awm.updateAppWidget(widgetID, rv);
			return;			
		}
		
		List<Subject> dayList = schedule.get(dayNum); 
		Log.d(TAG, "day nr " + dayNum);
		Log.d(TAG, dayList.toString());
		
		if (Build.VERSION.SDK_INT >= 12) {
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
		intentNext.setAction(SHOW_NEXT);
		intentPrev.setAction(SHOW_PREV);
		PendingIntent pIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, 0);
		PendingIntent pIntentPrev = PendingIntent.getBroadcast(context, 0, intentPrev, 0);
		views.setOnClickPendingIntent(R.id.right_arrow_btn, pIntentNext);
		views.setOnClickPendingIntent(R.id.left_arrow_btn, pIntentPrev);
		//wypełniamy widoki listy
		for (Subject sub : dayList) {

			RemoteViews innerView = new RemoteViews(context.getPackageName(), R.layout.row_layout);
			innerView.setTextViewText(R.id.row_time, sub.getStartTime() + " - " + sub.getStopTime());
			innerView.setTextViewText(R.id.row_label, sub.toString());
			views.addView(R.id.container, innerView);
		}
		awm.updateAppWidget(widgetID, views);
	}


	@TargetApi(12)
	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		
		if(intent.getAction().equals(SHOW_NEXT) || intent.getAction().equals(SHOW_PREV) ){
			
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.main_widget_layout);

			if (intent.getAction().equals(SHOW_NEXT)){
				
				dayNum = (++dayNum)%5;
				Intent i = new Intent(context,AnimHelperService.class);
				i.putExtra("direction", 1);
				context.startService(i);
			}
			else{
				dayNum = (--dayNum+5)%5;
				Intent i = new Intent(context,AnimHelperService.class);
				i.putExtra("direction", -1);
				context.startService(i);
			}
			if (Build.VERSION.SDK_INT >= 12){

				final ComponentName cn = new ComponentName(context,Widget.class);
		        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		        remoteViews.setDisplayedChild(R.id.flipper, dayNum);
		        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		        appWidgetManager.updateAppWidget(cn, remoteViews);
			}   
			else{
				//zeby wymusic funkcję onUpdate z super trzeba zmodyfikowac intent
				intent  = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, context, Widget.class);
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, Widget.class)));
			}
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "sam się usuń!!", Toast.LENGTH_LONG).show();

	}
	private void setButtonListeners(RemoteViews remoteViews, int appWidgetId){
		
		Intent intentNext = new Intent(context,Widget.class);
		Intent intentPrev = new Intent(context,Widget.class);
		intentNext.setAction(SHOW_NEXT);
		intentNext.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intentPrev.setAction(SHOW_PREV);
		intentPrev.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		PendingIntent pIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, 0);
		PendingIntent pIntentPrev = PendingIntent.getBroadcast(context, 0, intentPrev, 0);
		remoteViews.setOnClickPendingIntent(R.id.right_arrow_btn, pIntentNext);
		remoteViews.setOnClickPendingIntent(R.id.left_arrow_btn, pIntentPrev);
	}
	/**
	 * Wypełnienie widoku dla nowszych wersji poprzez ustawienie adaptera (RemoteViewsProvider to obudowany adapter)
	 * dla ListView  za pomocą WidgetService. Adapter pobiera dane, tworzy i zwraca widoki (RemoteViews) automatycznie
	 * pobierane przy wypełnianiu listy.
	 */
	@TargetApi(12)
	@SuppressWarnings("deprecation")
	private void updateWidgetAPI11(AppWidgetManager awm, int appWidgetId) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
		
		setButtonListeners(remoteViews, appWidgetId);
		
		int [] listViewList = { R.id.listView0, R.id.listView1, R.id.listView2, R.id.listView3, R.id.listView4};
		for (int i = 0 ; i<5;i++){
			
			awm.notifyAppWidgetViewDataChanged(appWidgetId, listViewList[i]);
			Intent serviceIntent = new Intent(context, WidgetService.class);
			serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			serviceIntent.putExtra("dayNum", i);
			serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
			remoteViews.setRemoteAdapter(appWidgetId, listViewList[i], serviceIntent);
			remoteViews.setEmptyView(listViewList[i], R.id.empty_listView);
			
		}
		mRemoteViews = remoteViews;
		remoteViews.setDisplayedChild(R.id.flipper, dayNum);
		awm.updateAppWidget(appWidgetId, remoteViews);
		Log.d("widget", "finishing update");
	}
	public static void setSchedule(Map<Integer, List<Subject> > schedule){
		Widget.schedule = schedule;
		for(List<Subject> dayList : schedule.values()){
			
			Collections.sort(dayList);

		}
		isEmpty = false;
	}
	public static Map<Integer, List<Subject> > getSchedule(){
		return Widget.schedule;
	}
}