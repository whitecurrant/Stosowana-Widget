package stosowana.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

public class DataFetchActivity extends Activity {
	
	private static final String TAG="DataFetchActivity"; //< It's useful in debugging
	
	private int widgetID;
	private Context context;
//	Zmienne odpowiedzialne za pobranie danych z REQUEST
	private final static String REQUEST = "http://arbus.home.pl/zapisy2013/api/index.php";
	private static String indexID;
	private static String passwd;
	private HttpClient client;
	private HttpPost post;
	private BufferedReader in = null;
	private String token = null;
	private String expires = null;
	private Long uuid;
	private String sid;
	private String scheduleID;
//	Parser JSON
	private JsonParserFactory factory;
	private JSONParser parser;
	
	private Map<Integer, List<Subject> > schedule;
	
	private String fileName = "/file.ser"; //< file with serialized data
	
	
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
		
		List<Subject> testList = schedule.get(0); // na razie tylko dla poniedziałku
		
		for(Subject sub:testList){
		
			RemoteViews innerView = new RemoteViews(context.getPackageName(), R.layout.row_layout);
			innerView.setTextViewText(R.id.row_time, sub.getStartTime()+" - "+sub.getStopTime());
			innerView.setTextViewText(R.id.row_label, sub.toString());
			views.addView(R.id.container, innerView);
			awm.updateAppWidget(widgetID, views);
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		boolean dontShowLoginMenu = true; 

//		deleteScheduleDir();// <- Don't touch me! I'm important!
				
		File file = new File(this.getFilesDir().getAbsolutePath() + "/schedule");
		// It's true only for very first start on mobile.
		if(!file.exists())
			file.mkdir();	
		// It's true when schedule exist
		else if(file.listFiles().length != 0){
			Log.d(TAG, Integer.toString(file.listFiles().length));
			Log.d(TAG, file.listFiles()[0].getPath());
			loadData(file);	
//			dontShowLoginMenu = true;
			
		}else{
//			setContentView(R.layout.data_fetch_layout);
			
//			to restore fetching data from internet, just replace this v on this ^
			
//		------------	
			schedule = new HashMap<Integer, List<Subject>>();
			List<Subject> list = new ArrayList<Subject>();
			list.add(new Subject("pierwsze","teacher", "12","08:00","09:30"));
			list.add(new Subject("drugie","teacher", "10","09:30","11:30"));
			schedule.put(0, list);
			
			saveData(file);
//		-----------	
			
		
		}
		
		super.onCreate(savedInstanceState);
		context = DataFetchActivity.this;
		setResult(RESULT_CANCELED);  	
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras !=null){	
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		else {  
			Log.d("invalid ID", "invalid ID!");
            finish();  
		}  
		
		if(dontShowLoginMenu){
			populateWidget();
			showWidget();
		}
			
		
	}
	
	private void saveData(File file) {
		Log.d(TAG, "saveData");
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try{
			fos = new FileOutputStream(file.getPath() + fileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(schedule);
		}catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(fos != null)
					fos.close();
				if(oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	private void loadData(File file) {
		Log.d(TAG, "loadData");
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
			fis = new FileInputStream(file.getPath() + fileName);
			ois = new ObjectInputStream(fis);
			schedule = (Map<Integer, List<Subject>>) ois.readObject();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(ois != null)
					ois.close();
				if(fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	private void deleteScheduleDir() {
		File dir = new File(this.getFilesDir().getAbsolutePath() + "/schedule");
		if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(dir, children[i]).delete();
	        }
	    }
	}
	public void fetch(View view){
		Log.d(TAG, "fetch");
		EditText mName = (EditText) findViewById(R.id.usernameField);
		EditText mPasswd = (EditText) findViewById(R.id.passwdField);
		Editable emName = mName.getText();
		Editable emPasswd = mPasswd.getText();
		
		indexID = emName.toString();
		passwd = emPasswd.toString();
		
		if(indexID.length() == 0 && passwd.length()==0)
			Toast.makeText(context, "Proszę podać nr indeksu oraz hasło", Toast.LENGTH_LONG).show();
		else if(indexID.length() != 6)
			Toast.makeText(context, "Niepoprawny format numeru indeksu", Toast.LENGTH_LONG).show();
		else if(indexID.length() == 0)
			Toast.makeText(context, "Proszę podać nr indeksu", Toast.LENGTH_LONG).show();
		else if(passwd.length() == 0)
			Toast.makeText(context, "Proszę podać hasło", Toast.LENGTH_LONG).show();
		else{
			connect();
			populateWidget();
			showWidget();
			}
	}

	private void connect() {
//		Log.d(TAG, "connect");
		
		client = new DefaultHttpClient();
		post = new HttpPost(REQUEST);
		uuid = UUID.randomUUID().getMostSignificantBits();
		schedule = new HashMap<Integer, List<Subject>>();
		
		factory = JsonParserFactory.getInstance();
		parser = factory.newJsonParser();
	
		try {
			
			getToken();
			login();		
			getToken();		
			listSchedule();		
			getToken();		
			getSchedule();
			getToken();		
			logout();
//			saveData(new File(this.getFilesDir().getAbsolutePath() + "/schedule"));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	

	}
	
	private void getToken() throws ClientProtocolException, IOException {
		String line = null;
		post.setEntity(new UrlEncodedFormEntity(makeGetToken()));
		HttpResponse response = client.execute(post);
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		line = in.readLine();
		Map jsonMap = parser.parseJson(line);
		switch(Integer.parseInt(jsonMap.get("code").toString())){
			case 200:
				token = jsonMap.get("token").toString();
				expires = jsonMap.get("expires").toString();
				break;
			case 400:
			case 401:
				//kod obslugi błędow
				break;
		}
	}
	
	private void login() throws ClientProtocolException, IOException, JSONException {
		
		String line = null;
		post.setEntity(new UrlEncodedFormEntity(makeLogin()));
		HttpResponse response = client.execute(post);
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		line = in.readLine();
//		Log.d(TAG+" login", line);
		JSONObject jsonObj = new JSONObject(line);
		switch(Integer.parseInt(jsonObj.getString("code"))){
			case 200:
				//do dokonczenia, teraz biore to co jest mi potrzebne do komunikacji
				sid = jsonObj.getString("session");
//				Log.d(TAG, sid);
				break;
			case 400:
			case 401:
				//kod obslugi błędow
				break;
		}
	}

	private void listSchedule() throws ClientProtocolException, IOException, JSONException {
	String line = null;
	post.setEntity(new UrlEncodedFormEntity(makeListSchedules()));
	HttpResponse response = client.execute(post);
	in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	line = in.readLine();
//	Log.d(TAG + "ListSchedue", line);	
	JSONObject jsonObj = new JSONObject(line);
	switch(Integer.parseInt(jsonObj.getString("code"))){
	case 200:
		//do dokonczenia, teraz biore to co jest mi potrzebne do komunikacji
		scheduleID = jsonObj.getJSONArray("scheduleList").getJSONObject(0).getString("scheduleID");
//		Log.d(TAG, scheduleID);
		break;
	case 400:
	case 401:
		//kod obslugi błędow
		break;
	}
}

	private void getSchedule() throws IllegalStateException, IOException, JSONException {
	String line = null;
	post.setEntity(new UrlEncodedFormEntity(makeGetSchedules()));
	HttpResponse response = client.execute(post);
	in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	line = in.readLine();
	JSONObject jsonObj = new JSONObject(line);
	switch(Integer.parseInt(jsonObj.getString("code"))){
		case 200:
//			Log.d(TAG, "cous");
//			Log.d(TAG,jsonObj.getJSONObject("schedule").getJSONArray("0").getJSONObject(0).getString("subjectName"));
			for(int i=0; i<jsonObj.getJSONObject("schedule").length(); i++){
					List<Subject> list = new ArrayList<Subject>();
					for (int j=0; j<jsonObj.getJSONObject("schedule").getJSONArray(Integer.toString(i)).length();j++) {
						Subject subject = new Subject();
						subject.setStartTime(jsonObj.getJSONObject("schedule").getJSONArray(Integer.toString(i)).getJSONObject(j).getString("start"));
						subject.setStopTime(jsonObj.getJSONObject("schedule").getJSONArray(Integer.toString(i)).getJSONObject(j).getString("end"));
						subject.setWeek(jsonObj.getJSONObject("schedule").getJSONArray(Integer.toString(i)).getJSONObject(j).getString("week"));
						subject.setClassroom(jsonObj.getJSONObject("schedule").getJSONArray(Integer.toString(i)).getJSONObject(j).getString("room"));
						subject.setTeacher(jsonObj.getJSONObject("schedule").getJSONArray(Integer.toString(i)).getJSONObject(j).getString("teacher"));
						subject.setName(jsonObj.getJSONObject("schedule").getJSONArray(Integer.toString(i)).getJSONObject(j).getString("subjectName"));
						subject.setType(jsonObj.getJSONObject("schedule").getJSONArray(Integer.toString(i)).getJSONObject(j).getString("type"));
						list.add(subject);
					}
					
				schedule.put(Integer.valueOf(i), list);
			}
			
			break;
		case 400:
		case 401:
			//kod obslugi błędow
			break;
	}
}

	private void logout() throws ClientProtocolException, IOException {
		String line = null;
		post.setEntity(new UrlEncodedFormEntity(makeLogout()));
		HttpResponse response = client.execute(post);
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		line = in.readLine();
		Map jsonMap = parser.parseJson(line);
		switch(Integer.parseInt(jsonMap.get("code").toString())){
			case 200:
//				Log.d(TAG, jsonMap.toString());
				break;
			case 400:
			case 401:
				//kod obslugi błędow
				break;
		}
	}

	public List<NameValuePair> makeGetToken(){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("call", "getToken"));
		nameValuePairs.add(new BasicNameValuePair("args[indexID]", indexID));
		nameValuePairs.add(new BasicNameValuePair("args[uuid]", Long.toString(uuid)));
		
		return nameValuePairs;
	}
	private List<NameValuePair> makeLogin() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("call", "login"));
		nameValuePairs.add(new BasicNameValuePair("args[indexID]", indexID));
		nameValuePairs.add(new BasicNameValuePair("args[password]", passwd));
		nameValuePairs.add(new BasicNameValuePair("args[uuid]", Long.toString(uuid)));
		nameValuePairs.add(new BasicNameValuePair("args[token]", token));
		
		return nameValuePairs;
	}
	private List<NameValuePair> makeListSchedules() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("call", "listSchedules"));
		nameValuePairs.add(new BasicNameValuePair("args[sid]", sid));
		nameValuePairs.add(new BasicNameValuePair("args[uuid]", Long.toString(uuid)));
		nameValuePairs.add(new BasicNameValuePair("args[token]", token));
		
		return nameValuePairs;
	}
	private List<NameValuePair> makeGetSchedules() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("call", "getSchedule"));
		nameValuePairs.add(new BasicNameValuePair("args[sid]", sid));
		nameValuePairs.add(new BasicNameValuePair("args[uuid]", Long.toString(uuid)));
		nameValuePairs.add(new BasicNameValuePair("args[token]", token));
		nameValuePairs.add(new BasicNameValuePair("args[scheduleID]", scheduleID));
		
		return nameValuePairs;
	}
	private List<NameValuePair> makeLogout() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("call", "logout"));
		nameValuePairs.add(new BasicNameValuePair("args[sid]", sid));
		nameValuePairs.add(new BasicNameValuePair("args[uuid]", Long.toString(uuid)));
		nameValuePairs.add(new BasicNameValuePair("args[token]", token));
		
		return nameValuePairs;
	}
	
}

