package sk.thenoen.yukebox.httpserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.ThumbnailDetails;

import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import sk.thenoen.yukebox.service.YoutubeService;
import sk.thenoen.yukebox.domain.Result;
import sk.thenoen.yukebox.domain.SearchResponse;

public class SearchController extends RouterNanoHTTPD.GeneralHandler {

	public static final String ROUTE_MAPPING = "/api/search";
	public static final int ROUTE_PRIORITY = 11;

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
			result.setThumbnailUrl(getThumbnailUrlString(searchResult));
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

	private String getThumbnailUrlString(SearchResult searchResult) {
		ThumbnailDetails thumbnailDetails = searchResult.getSnippet().getThumbnails();
		if (thumbnailDetails.getMedium() != null) {
			return thumbnailDetails.getMedium().getUrl();
		}
		if (thumbnailDetails.getStandard() != null) {
			return thumbnailDetails.getStandard().getUrl();
		}
		return thumbnailDetails.getDefault().getUrl();
	}

}
