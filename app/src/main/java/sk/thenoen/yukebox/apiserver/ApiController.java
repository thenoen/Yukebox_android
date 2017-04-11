package sk.thenoen.yukebox.apiserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;

import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import sk.thenoen.yukebox.YoutubeService;
import sk.thenoen.yukebox.domain.Result;
import sk.thenoen.yukebox.domain.SearchResponse;

class ApiController extends RouterNanoHTTPD.GeneralHandler {

	@Override
	public String getMimeType() {
		return "text/html";
	}

	@Override
	public NanoHTTPD.Response.IStatus getStatus() {
		return NanoHTTPD.Response.Status.OK;
	}

	@Override
	public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
		StringBuilder text = new StringBuilder("<html><body>");
		text.append("<h1>Url: ");
		text.append(session.getUri());
		text.append("</h1><br>");
		Map<String, String> queryParams = session.getParms();
		if (queryParams.size() > 0) {
			for (Map.Entry<String, String> entry : queryParams.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				text.append("<p>Param '");
				text.append(key);
				text.append("' = ");
				text.append(value);
				text.append("</p>");
			}
		} else {
			text.append("<p>no params in url</p><br>");
		}

		YoutubeService youtubeService = YoutubeService.getInstance();
		List<SearchResult> searchResults = youtubeService.search(session.getParameters().get("query").get(0));

		String jsongResponse = writeObjectToString(searchResults);
		System.out.println(Thread.currentThread().getName());
		return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), jsongResponse);
	}

	private String writeObjectToString(List<SearchResult> searchResults) {
		SearchResponse searchResponse = new SearchResponse();
		searchResponse.setSuccess(true);

		for (SearchResult searchResult : searchResults) {
			Result result = new Result();
			result.setVideoId(searchResult.getId().getVideoId());
			result.setName(searchResult.getSnippet().getTitle());
			result.setDescription(searchResult.getSnippet().getDescription());
			searchResponse.getResults().add(result);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(searchResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
