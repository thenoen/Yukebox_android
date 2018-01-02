package sk.thenoen.yukebox.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import sk.thenoen.yukebox.R;
import sk.thenoen.yukebox.database.dao.VideoDao;
import sk.thenoen.yukebox.httpserver.ApiHttpServer;
import sk.thenoen.yukebox.httpserver.controller.MediaPlayerController;
import sk.thenoen.yukebox.service.MediaPlayer;
import sk.thenoen.yukebox.service.YoutubeApiService;
import sk.thenoen.yukebox.service.YoutubeUtils;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	private Handler guiUpdateHandler = new Handler();

	@Inject
	ApiHttpServer apiHttpServer;

	@Inject
	@Named("serverPort")
	int serverPort;

	@Inject
	YoutubeApiService youtubeApiService;

	@Inject
	VideoDao videoDao;

	@BindView(R.id.player_activity_content_root)
	ConstraintLayout contentViewConstraintLayout;

	@BindView(R.id.log_text)
	TextView statusText;

	@BindView(R.id.url_text)
	TextView urlText;

	@BindView(R.id.copy_url_button)
	ImageButton copyUrlButton;

	@BindView(R.id.youtube_view)
	YouTubePlayerView youTubePlayerView;

	private ConstraintSet landscapeConstraintSet;
	private ConstraintSet portraitConstraintSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AndroidInjection.inject(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_activity_layout);
		ButterKnife.bind(this);

		startHttpServer();
		displayServerEndpoint();
		initYoutubePlayer();

		portraitConstraintSet = createPortraitConstraintSet();
		landscapeConstraintSet = createLandscapeConstraintSet();

		rotateView(getResources().getConfiguration().orientation);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		rotateView(newConfig.orientation);
	}

	private void rotateView(int orientation) {
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			portraitConstraintSet.applyTo(contentViewConstraintLayout);
		} else {
			landscapeConstraintSet.applyTo(contentViewConstraintLayout);
		}
	}

	private ConstraintSet createPortraitConstraintSet() {
		ConstraintSet landscapeConstraintSet = new ConstraintSet();
		landscapeConstraintSet.clone(contentViewConstraintLayout);
		return landscapeConstraintSet;
	}

	private ConstraintSet createLandscapeConstraintSet() {
		ConstraintSet portraitConstraintSet = new ConstraintSet();
		portraitConstraintSet.clone(contentViewConstraintLayout);
		portraitConstraintSet.clone(portraitConstraintSet);

		portraitConstraintSet.clear(R.id.youtube_view);
		portraitConstraintSet.clear(R.id.log_text);

		int margin = (int) getResources().getDimension(R.dimen.default_margin);
		portraitConstraintSet.connect(R.id.youtube_view, ConstraintSet.TOP, R.id.url_title, ConstraintSet.BOTTOM, margin);
		portraitConstraintSet.connect(R.id.log_text, ConstraintSet.TOP, R.id.url_title, ConstraintSet.BOTTOM, margin);

		portraitConstraintSet.connect(R.id.youtube_view, ConstraintSet.START, R.id.player_activity_content_root, ConstraintSet.START, 0);
		portraitConstraintSet.connect(R.id.log_text, ConstraintSet.END, R.id.player_activity_content_root, ConstraintSet.END, 0);

		portraitConstraintSet.connect(R.id.log_text, ConstraintSet.LEFT, R.id.youtube_view, ConstraintSet.RIGHT, 0);
		portraitConstraintSet.connect(R.id.youtube_view, ConstraintSet.RIGHT, R.id.log_text, ConstraintSet.LEFT, 0);

		portraitConstraintSet.createHorizontalChain(R.id.youtube_view, ConstraintSet.RIGHT, R.id.log_text, ConstraintSet.LEFT,
				new int[]{R.id.youtube_view, R.id.log_text}, null, ConstraintWidget.CHAIN_SPREAD);

		//todo weights should be defined in createHorizontalChain() but there is a bug in version 1.0.2 of ConstraintLayout
		//todo these weights can cause problems on devices with small resolution - there limit for minimal dimensions of youtube view (px)
		portraitConstraintSet.setHorizontalWeight(R.id.youtube_view, 1f);
		portraitConstraintSet.setHorizontalWeight(R.id.log_text, 2f);

		portraitConstraintSet.constrainWidth(R.id.youtube_view, ConstraintSet.MATCH_CONSTRAINT);
		portraitConstraintSet.constrainWidth(R.id.log_text, ConstraintSet.MATCH_CONSTRAINT);

		portraitConstraintSet.constrainHeight(R.id.youtube_view, ConstraintSet.WRAP_CONTENT);
		portraitConstraintSet.constrainHeight(R.id.log_text, ConstraintSet.WRAP_CONTENT);

		return portraitConstraintSet;
	}

	private void displayServerEndpoint() {
		WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
		if (wifiManager == null) {
			urlText.setText(getString(R.string.server_endpoint_cannot_be_obtained));
			return;
		}
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
		final String formattedIpAddress = String.format(Locale.getDefault(), "%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

		urlText.setText(getString(R.string.server_endpoint, formattedIpAddress, serverPort));
	}

	private void startHttpServer() {
		try {
			apiHttpServer.start();
		} catch (IOException e) {
			updateStatusText("Could not start server: " + e.getMessage());
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
		youTubePlayerView.initialize(YoutubeUtils.getYoutubeApiKey(), this);
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
		MediaPlayer mediaPlayer = new MediaPlayer(youTubePlayer);
		Object[] parameters = {mediaPlayer};
		apiHttpServer.addMapping(MediaPlayerController.ROUTE_MAPPING, MediaPlayerController.ROUTE_PRIORITY, MediaPlayerController.class, parameters);
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
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	@OnClick(R.id.copy_url_button)
	public void copyTextToClipboard() {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		if (clipboard == null) {
			Toast.makeText(this, getText(R.string.url_cannot_be_copied_toast), Toast.LENGTH_LONG)
					.show();
			return;
		}
		ClipData clip = ClipData.newPlainText(getText(R.string.url_copy_label), urlText.getText());
		clipboard.setPrimaryClip(clip);

		Toast.makeText(this, getText(R.string.url_copied_toast), Toast.LENGTH_LONG)
				.show();
	}
}
