package sk.thenoen.yukebox.activity;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import sk.thenoen.yukebox.R;
import sk.thenoen.yukebox.httpserver.controller.MediaPlayerController;
import sk.thenoen.yukebox.httpserver.ApiHttpServer;
import sk.thenoen.yukebox.service.MediaPlayer;
import sk.thenoen.yukebox.service.YoutubeUtils;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	public static final int SERVER_PORT = 9090;
	private ApiHttpServer apiHttpServer;

	private Handler guiUpdateHandler = new Handler();
	private EditText statusText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logging);
		statusText = (EditText) findViewById(R.id.status_text);

		File wwwDirectory = new File(getFilesDir(), getResources().getString(R.string.www_dir_name));
		startHttpServer(wwwDirectory);
		displayServerEndpoint();
		initYoutubePlayer();
	}

	private void displayServerEndpoint() {
		WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
		final String formatedIpAddress = String.format(Locale.getDefault(),
				"%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

		EditText editText = (EditText) findViewById(R.id.endpoint_text);
		editText.setText(getString(R.string.server_endpoint, formatedIpAddress, SERVER_PORT));
	}

	private void startHttpServer(File wwwDirectory) {
		apiHttpServer = new ApiHttpServer(SERVER_PORT, wwwDirectory);
		try {
			apiHttpServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		if (apiHttpServer != null) {
			apiHttpServer.stop();
		}
		super.onDestroy();
	}

	private void initYoutubePlayer() {
		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize(YoutubeUtils.getYoutubeApiKey(), this);
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
		MediaPlayer mediaPlayer = new MediaPlayer(youTubePlayer);
		apiHttpServer.addMapping(MediaPlayerController.ROUTE_MAPPING, MediaPlayerController.ROUTE_PRIORITY, MediaPlayerController.class, mediaPlayer);
		guiUpdateHandler.post(new Runnable() {
			@Override
			public void run() {
				updateStatusText(getResources().getString(R.string.server_initialization_status_ok));
			}
		});

	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
		EditText editText = (EditText) findViewById(R.id.status_text);
		editText.append(youTubeInitializationResult.toString());
	}

	private void updateStatusText(String text) {
		statusText.setText(text);
	}
}
