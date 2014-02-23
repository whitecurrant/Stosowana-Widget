package stosowana.schedule;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DataFetchActivity extends Activity {
	
	private static String nrIndex;
	private static String passwd;
	private final static String REQUEST = "http://arbus.home.pl/zapisy2013/api/index.php";
	private final static String indexID = "args[IndexID]=";
	private final static String uuid = "args[uuid]";
	private static final String TAG="DataFetchActivity"; //< It's useful in debugging
	private int widgetID;
	private Context context;
	
	private void showWidget(){
		
		Intent startIntent = new Intent(DataFetchActivity.this,Widget.class);
		startIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
		startIntent.setAction("from data fetch activity");
		setResult(RESULT_OK, startIntent);
		startService(startIntent);
		finish();
		
	}
	private void populateWidget(){
		
		AppWidgetManager awm = AppWidgetManager.getInstance(context);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget_layout);
		
		/*for (int i = 0;i<5;i++){
			RemoteViews innerView = new RemoteViews(context.getPackageName(), R.layout.row_layout);
			innerView.setTextViewText(R.id.row_time, "1"+i);
			innerView.setTextViewText(R.id.row_label, "o ja pierdole" + i*2);
			views.addView(R.id.container, innerView);
			awm.updateAppWidget(widgetID, views);
		}*/
		
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.data_fetch_layout);
		super.onCreate(savedInstanceState);
		context = DataFetchActivity.this;
		setResult(RESULT_CANCELED);  	
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras !=null){
			
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		else {  
			System.out.println("invalid id");
			Log.d("invalid ID", "invalid ID!");
            finish();  
		}  
		
	}
	
	public void fetch(View view){
		/*
		EditText mName = (EditText) findViewById(R.id.usernameField);
		EditText mPasswd = (EditText) findViewById(R.id.passwdField);
		Editable emName = mName.getText();
		Editable emPasswd = mPasswd.getText();
		
		nrIndex = emName.toString();
		passwd = emPasswd.toString();
		
		if(nrIndex.length() == 0 && passwd.length()==0)
			Toast.makeText(context, "Proszę podać nr indeksu oraz hasło", Toast.LENGTH_LONG).show();
		else if(nrIndex.length() != 6)
			Toast.makeText(context, "Niepoprawny format numeru indeksu", Toast.LENGTH_LONG).show();
		else if(nrIndex.length() == 0)
			Toast.makeText(context, "Proszę podać nr indeksu", Toast.LENGTH_LONG).show();
		else if(passwd.length() == 0)
			Toast.makeText(context, "Proszę podać hasło", Toast.LENGTH_LONG).show();
		else{
		*/
			connect();
			populateWidget();
			showWidget();
		//}
	}

	private void connect() {
		
		URL url = null;
		HttpURLConnection connection = null;
		Long uuid = UUID.randomUUID().getMostSignificantBits();
		
		
		try {
			url = new URL(REQUEST);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
		} catch (MalformedURLException e ) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
