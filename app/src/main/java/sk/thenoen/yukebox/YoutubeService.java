package sk.thenoen.yukebox;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class YoutubeService {

	private static final String PROPERTIES_CLASSPATH = "/youtube.properties";
	private static final long NUMBER_OF_VIDEOS_RETURNED = 10;

	// https://developers.google.com/youtube/v3/docs/search/list#type
	private static final String SEARCH_TYPE = "video";

	private static YoutubeService instance;

	private YouTube youtube;

	private YoutubeService() {
	}

	public static synchronized YoutubeService getInstance() {
		if(instance == null) {
			instance = new YoutubeService();
		}
		return instance;
	}

	public List<SearchResult> search(String queryTerm) {

		Properties properties = new Properties();
		try {
			InputStream in = YoutubeService.class.getResourceAsStream(PROPERTIES_CLASSPATH);
			properties.load(in);

		} catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_CLASSPATH + ": " + e.getCause()
					+ " : " + e.getMessage());
			System.exit(1);
		}

		youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName("youtube-cmdline-search-sample").build();

		// Define the API request for retrieving search results.
		YouTube.Search.List search = null;
		try {
			search = youtube.search().list("id,snippet");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String apiKey = properties.getProperty("youtube.apikey");
		search.setKey(apiKey);
		search.setQ(queryTerm);
		search.setType(SEARCH_TYPE);

		// To increase efficiency, only retrieve the fields that the application uses.
//		search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url,snippet/*)");
		search.setFields("items(*)");
		search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

		// Call the API and print results.
		SearchListResponse searchResponse = null;
		try {
			searchResponse = search.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<SearchResult> searchResultList = searchResponse.getItems();
		if (searchResultList != null) {
			System.out.println(searchResultList);
		}

		return searchResultList;
	}
}
