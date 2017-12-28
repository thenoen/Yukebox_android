package sk.thenoen.yukebox.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import sk.thenoen.yukebox.R;
import sk.thenoen.yukebox.database.dao.VideoDao;
import sk.thenoen.yukebox.httpserver.ApiHttpServer;
import sk.thenoen.yukebox.httpserver.controller.MediaPlayerController;
import sk.thenoen.yukebox.service.MediaPlayer;
import sk.thenoen.yukebox.service.YoutubeUtils;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	public static final int SERVER_PORT = 9090;
	private ApiHttpServer apiHttpServer;

	private Handler guiUpdateHandler = new Handler();

	@BindView(R.id.log_text)
	TextView statusText;

	@BindView(R.id.url_text)
	TextView urlText;

	@BindView(R.id.copy_url_button)
	ImageButton copyUrlButton;

	@Inject
	VideoDao videoDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AndroidInjection.inject(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_activity_layout);
		ButterKnife.bind(this);

		File wwwDirectory = new File(getFilesDir(), getResources().getString(R.string.www_dir_name));
		startHttpServer(wwwDirectory);
		displayServerEndpoint();
		initYoutubePlayer();
	}

	private void displayServerEndpoint() {
		WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
		final String formatedIpAddress = String.format(Locale.getDefault(), "%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

		urlText.setText(getString(R.string.server_endpoint, formatedIpAddress, SERVER_PORT));
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
		guiUpdateHandler.post(() -> updateStatusText(getResources().getString(R.string.server_initialization_status_ok)));
	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
		statusText.append(youTubeInitializationResult.toString());
	}

	private void updateStatusText(String text) {
		statusText.append(text + "\n");
	}

	@Override
	public void onBackPressed () {
		moveTaskToBack (true);
	}

	@OnClick(R.id.copy_url_button)
	public void copyTextToClipboard() {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText(getText(R.string.url_copy_label), urlText.getText());
		clipboard.setPrimaryClip(clip);

		Toast toast = Toast.makeText(this, getText(R.string.url_copied_toast), Toast.LENGTH_LONG);
		toast.show();
	}
}
