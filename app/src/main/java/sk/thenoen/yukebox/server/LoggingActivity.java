package sk.thenoen.yukebox.server;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import fi.iki.elonen.SimpleWebServer;
import fi.iki.elonen.util.ServerRunner;
import sk.thenoen.yukebox.R;

public class LoggingActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logging);

		int port = 8080;

		WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
		final String formatedIpAddress = String.format(Locale.getDefault(),
				"%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

		EditText editText = (EditText) findViewById(R.id.logging_text);
		editText.setText("Please access! http://" + formatedIpAddress + ":" + port);

		File wwwDirectory = new File(getFilesDir(), getResources().getString(R.string.www_dir_name));
		SimpleWebServer simpleWebServer = new SimpleWebServer(null, 8080, wwwDirectory, false);

		try {
			simpleWebServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
