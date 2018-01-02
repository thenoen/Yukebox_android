package sk.thenoen.yukebox;

import org.junit.Ignore;
import org.junit.Test;

import sk.thenoen.yukebox.service.YoutubeApiService;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
	@Test
	public void addition_isCorrect() throws Exception {
		assertEquals(4, 2 + 2);
	}

	@Ignore("it is using real youtube api")
	@Test
	public void verifyYoutubeService() throws Exception {
		YoutubeApiService youtubeApiService = YoutubeApiService.getInstance("package.name", "sha1");
		youtubeApiService.search("song of silence");
	}
}