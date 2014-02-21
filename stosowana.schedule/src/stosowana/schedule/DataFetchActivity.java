package stosowana.schedule;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DataFetchActivity extends Activity {
	private static String nrIndex;
	private static String passwd;
	private final static String REQUEST = "http://arbus.home.pl/zapisy2013/api/index.php";
	private final static String indexID = "args[IndexID]=";
	private final static String uuid = "args[uuid]";
	
	private static final String TAG="DataFetchActivity"; //< It's useful in debugging
	
	
	public DataFetchActivity() {
		
	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_fetch_layout);

	}
	
	public void fetch(View view){
		EditText mName = (EditText) findViewById(R.id.usernameField);
		EditText mPasswd = (EditText) findViewById(R.id.passwdField);
		Editable emName = mName.getText();
		Editable emPasswd = mPasswd.getText();
		
		nrIndex = emName.toString();
		passwd = emPasswd.toString();
		
		if(nrIndex.length() == 0 && passwd.length()==0)
			Toast.makeText(getApplicationContext(), "Proszę podać nr indeksu oraz hasło", Toast.LENGTH_LONG).show();
		else if(nrIndex.length() != 6)
			Toast.makeText(getApplicationContext(), "Niepoprawny format numeru indeksu", Toast.LENGTH_LONG).show();
		else if(nrIndex.length() == 0)
			Toast.makeText(getApplicationContext(), "Proszę podać nr indeksu", Toast.LENGTH_LONG).show();
		else if(passwd.length() == 0)
			Toast.makeText(getApplicationContext(), "Proszę podać hasło", Toast.LENGTH_LONG).show();
		else
			connect();

	
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
