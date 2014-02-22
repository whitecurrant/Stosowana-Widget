package stosowana.schedule;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import com.json.serializers.ListSerializer;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DataFetchActivity extends Activity {
	
	private static final String TAG="DataFetchActivity"; //< It's useful in debugging
	
	private AppWidgetManager awm;
	private int widgetID;
	private Context context;
//	Zmienne odpowiedzialne za pobranie danych z REQUEST
	private final static String REQUEST = "http://arbus.home.pl/zapisy2013/api/index.php";
	private static String indexID;
	private static String passwd;
	private HttpClient client;
	private HttpPost post;
	private DataOutputStream out = null;
	private BufferedReader in = null;
	private String token = null;
	private String expires = null;
	private Long uuid;
	private String sid;
	private String scheduleID;
//	Parser JSON
	private JsonParserFactory factory;
	private JSONParser parser;
	
	
/*	private void populateData(){
		
		RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.main_widget_layout);
		views.setTextViewText(R.id.widget_textview,"sdafjfhyf");
		awm.updateAppWidget(widgetID, views);
	}*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_fetch_layout);
		context = DataFetchActivity.this;
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras !=null){
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		awm = AppWidgetManager.getInstance(context);
	}
	
	public void fetch(View view){
//		Log.d(TAG,"fetch"); 
		
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
			finish(); //konczy Activity
		}
		

	}

	private void connect() {
//		Log.d(TAG, "connect");
		
		client = new DefaultHttpClient();
		post = new HttpPost(REQUEST);
		uuid = UUID.randomUUID().getMostSignificantBits();
		
		JSONObject jsonObj;
		factory = JsonParserFactory.getInstance();
		parser = factory.newJsonParser();
		
		Map jsonMap = null;
		
		try {
			jsonMap = getToken();
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
			
			jsonObj= login();
			switch(Integer.parseInt(jsonObj.getString("code"))){
				case 200:
					//do dokonczenia, teraz biore to co jest mi potrzebne do komunikacji
					sid = jsonObj.getString("session");
//					Log.d(TAG, sid);
					break;
				case 400:
				case 401:
					//kod obslugi błędow
					break;
			}
			
			jsonMap = getToken();
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
			
			jsonObj = ListSchedule();
			switch(Integer.parseInt(jsonObj.getString("code"))){
			case 200:
				//do dokonczenia, teraz biore to co jest mi potrzebne do komunikacji
				scheduleID = jsonObj.getJSONArray("scheduleList").getJSONObject(0).getString("scheduleID");
				Log.d(TAG, scheduleID);
				break;
			case 400:
			case 401:
				//kod obslugi błędow
				break;
			}
			
			jsonMap = getToken();
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
			jsonObj = getSchedule();
			switch(Integer.parseInt(jsonObj.getString("code"))){
				case 200:
					Log.d(TAG, "getSchedule");
					break;
				case 400:
				case 401:
					//kod obslugi błędow
					break;
			}
			jsonMap = getToken();
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
			
			jsonMap = logout();
			switch(Integer.parseInt(jsonMap.get("code").toString())){
				case 200:
					Log.d(TAG, jsonMap.toString());
					break;
				case 400:
				case 401:
					//kod obslugi błędow
					break;
			}
		
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		

	}
	
	private Map getToken() throws ClientProtocolException, IOException{
		String line = null;
		post.setEntity(new UrlEncodedFormEntity(makeGetToken()));
		HttpResponse response = client.execute(post);
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		line = in.readLine();
//		Log.d(TAG+" getToken",line);
		return parser.parseJson(line);	
	}
	
	private JSONObject login() throws ClientProtocolException, IOException, JSONException {
		String line = null;
		post.setEntity(new UrlEncodedFormEntity(makeLogin()));
		HttpResponse response = client.execute(post);
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		line = in.readLine();
//		Log.d(TAG+" login", line);
		return new JSONObject(line);
	}
	
	private JSONObject ListSchedule() throws ClientProtocolException, IOException, JSONException {
		String line = null;
		post.setEntity(new UrlEncodedFormEntity(makeListSchedules()));
		HttpResponse response = client.execute(post);
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		line = in.readLine();
//		Log.d(TAG + "ListSchedue", line);
		return new JSONObject(line);	
	}
	
	private JSONObject getSchedule() throws IllegalStateException, IOException, JSONException {
		String line = null;
		post.setEntity(new UrlEncodedFormEntity(makeGetSchedules()));
		HttpResponse response = client.execute(post);
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		line = in.readLine();
		return new JSONObject(line);	
	}

	private Map logout() throws ClientProtocolException, IOException {
		String line = null;
		post.setEntity(new UrlEncodedFormEntity(makeLogout()));
		HttpResponse response = client.execute(post);
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		line = in.readLine();
		return parser.parseJson(line);
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

