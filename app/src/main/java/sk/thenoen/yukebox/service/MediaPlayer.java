package sk.thenoen.yukebox.service;

import com.google.android.youtube.player.YouTubePlayer;

public class MediaPlayer {

	private YouTubePlayer youTubePlayer;

	public MediaPlayer(YouTubePlayer youTubePlayer) {
		this.youTubePlayer = youTubePlayer;
	}

	public void playVideo(String videoId) {
		youTubePlayer.setPlayerStateChangeListener(
				new YouTubePlayer.PlayerStateChangeListener() {
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
				}
		);
		youTubePlayer.cueVideo(videoId);
	}

	public int getCurrentTimeMillis() {
		return youTubePlayer.getCurrentTimeMillis();
	}
}
