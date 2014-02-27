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
import android.view.View;
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
	private	int [] containers4lowAPI = {R.id.container0, R.id.container1, R.id.container2, R.id.container3, R.id.container4};
	private boolean dataChanged = true;
	private static boolean lectures = true;
	private static boolean laboratories = true;
	private static boolean excercise = true;
	
	
	
	/**
	 * Przy okazji update'u widgetu sprawdzamy czy nie jest pusty  a po sprawdzeniu
	 * wersji wywołujemy odpowiednią funkcję updatującą dla każdej instancji widgetu (ktoś mógł ich dodać kilka) 
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager awm,int[] appWidgetIds) {

		this.context = context;
		
		Log.d(TAG, "updating...");

		if (isEmpty){
			return;			
		}
		updateCurrentDay();
		List<Subject> dayList = schedule.get(dayNum); 
		
		if (Build.VERSION.SDK_INT >= 12) {
			Log.d(TAG, "for api >= 12");
			for (int widgetID : appWidgetIds)
				updateWidget4highAPI(awm, widgetID);
		} else {
			Log.d(TAG, "for api  < 12");
			for (int widgetID : appWidgetIds)
				updateWidget4lowAPI(awm, widgetID, dayList);
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
	private void updateWidget4lowAPI(AppWidgetManager awm, int appWidgetId, List<Subject> dayList) {

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
	
		setButtonListeners(views, appWidgetId);
		
		for(int i = 0 ; i <5 ; i++ ){
			
			List<Subject> oneDayList = schedule.get(i);
			for (Subject sub : oneDayList){
				
				RemoteViews innerView = new RemoteViews(context.getPackageName(), R.layout.row_layout);
				innerView.setTextViewText(R.id.row_time, sub.getStartTime() + " - " + sub.getStopTime());
				innerView.setTextViewText(R.id.row_label, sub.toString());
				views.addView(containers4lowAPI[i], innerView);
			}
			views.setViewVisibility(containers4lowAPI[i], View.GONE);
		}
		views.setViewVisibility(containers4lowAPI[dayNum], View.VISIBLE);
		awm.updateAppWidget(appWidgetId, views);
	}
	
	/**
	 * Wypełnienie widoku dla nowszych wersji poprzez ustawienie adaptera (RemoteViewsProvider to obudowany adapter)
	 * dla ListView  za pomocą WidgetService. Adapter pobiera dane, tworzy i zwraca widoki (RemoteViews) automatycznie
	 * pobierane przy wypełnianiu listy.
	 */
	@TargetApi(12)
	@SuppressWarnings("deprecation")
	private void updateWidget4highAPI(AppWidgetManager awm, int appWidgetId) {

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
		remoteViews.setDisplayedChild(R.id.flipper, dayNum);
		awm.updateAppWidget(appWidgetId, remoteViews);
		Log.d("widget", "finishing update");
	}

	@TargetApi(12)
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(SHOW_NEXT) || intent.getAction().equals(SHOW_PREV) ){
			
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.main_widget_layout);
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			final ComponentName cn = new ComponentName(context,Widget.class);

			if (intent.getAction().equals(SHOW_NEXT)){
				
				dayNum = (++dayNum)%5;
				Log.d(TAG, "showing next..");
			}
			else{
				
				dayNum = (--dayNum+5)%5;
				Log.d(TAG, "showing prev..");
			}
			if (Build.VERSION.SDK_INT >= 12){

		        remoteViews.setDisplayedChild(R.id.flipper, dayNum);
			}   
			else{
				
				for (int id : containers4lowAPI){
					remoteViews.setViewVisibility(id, View.GONE);
				}
				remoteViews.setViewVisibility(containers4lowAPI[dayNum], View.VISIBLE);
			}
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
			appWidgetManager.updateAppWidget(cn, remoteViews);
		}
		super.onReceive(context, intent);
	}

	private void setButtonListeners(RemoteViews remoteViews, int appWidgetId){
		
		Intent intentNext = new Intent(context,Widget.class);
		Intent intentPrev = new Intent(context,Widget.class);
		Intent intentMenu = new Intent(context,MenuActivity.class);

		intentNext.setAction(SHOW_NEXT);
		intentNext.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intentPrev.setAction(SHOW_PREV);
		intentPrev.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		PendingIntent pIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, 0);
		PendingIntent pIntentPrev = PendingIntent.getBroadcast(context, 0, intentPrev, 0);
		PendingIntent pMenu = PendingIntent.getActivity(context, 0, intentMenu, 0);

		remoteViews.setOnClickPendingIntent(R.id.right_arrow_btn, pIntentNext);
		remoteViews.setOnClickPendingIntent(R.id.left_arrow_btn, pIntentPrev);
		remoteViews.setOnClickPendingIntent(R.id.settings_bttn, pMenu);
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
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "pff, obyś nie zdał(a)!!", Toast.LENGTH_LONG).show();

	}
}
