package sk.thenoen.yukebox.server;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.services.youtube.YouTube;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.SimpleWebServer;
import fi.iki.elonen.util.ServerRunner;
import sk.thenoen.yukebox.R;
import sk.thenoen.yukebox.YoutubeService;
import sk.thenoen.yukebox.apiserver.ApiServer;

public class LoggingActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	public static final int SERVER_PORT = 8080;
	private SimpleWebServer simpleWebServer;
	private ApiServer apiServer;


	private YouTubePlayerView youTubeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logging);

		WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
		final String formatedIpAddress = String.format(Locale.getDefault(),
				"%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

		EditText editText = (EditText) findViewById(R.id.logging_text);
		editText.setText("Please access! http://" + formatedIpAddress + ":" + SERVER_PORT);

		File wwwDirectory = new File(getFilesDir(), getResources().getString(R.string.www_dir_name));
		simpleWebServer = new SimpleWebServer(null, 8080, wwwDirectory, false);

		try {
			simpleWebServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		apiServer = new ApiServer(9090);
		try {
			apiServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		initYoutubePlayer();
	}

	@Override
	protected void onDestroy() {
		if (simpleWebServer != null) {
			simpleWebServer.stop();
		}
		if (apiServer != null) {
			apiServer.stop();
		}
		super.onDestroy();
	}

	private void initYoutubePlayer() {
		youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize(YoutubeUtils.getYoutubeApiKey(), this);
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
		youTubePlayer.cueVideo("1JZnj4eNHXE");
		youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
			@Override
			public void onLoading() {

			}

			@Override
			public void onLoaded(String s) {
				youTubePlayer.play();
			}

			@Override
			public void onAdStarted() {

			}

			@Override
			public void onVideoStarted() {

			}

			@Override
			public void onVideoEnded() {

			}

			@Override
			public void onError(YouTubePlayer.ErrorReason errorReason) {

			}
		});
	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
		EditText editText = (EditText) findViewById(R.id.logging_text);
		editText.append(youTubeInitializationResult.toString());
	}
}
