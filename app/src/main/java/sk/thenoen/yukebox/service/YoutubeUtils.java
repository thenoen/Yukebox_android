package sk.thenoen.yukebox.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class YoutubeUtils {

	private static final String PROPERTIES_CLASSPATH = "/youtube.properties";
	public static final String YOUTUBE_APIKEY = "youtube.apikey";

	private YoutubeUtils() {

	}

	private static String youtubeKey;

	public static synchronized String getYoutubeApiKey() {
		if (youtubeKey == null) {
			youtubeKey = loadYoutubeApiKey();
		}
		return youtubeKey;
	}

	private static String loadYoutubeApiKey() {
		Properties properties = new Properties();
		try {
			InputStream in = YoutubeApiService.class.getResourceAsStream(PROPERTIES_CLASSPATH);
			properties.load(in);

		} catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_CLASSPATH + ": " + e.getCause()
					+ " : " + e.getMessage());
		}
		return properties.getProperty(YOUTUBE_APIKEY);
	}
}
