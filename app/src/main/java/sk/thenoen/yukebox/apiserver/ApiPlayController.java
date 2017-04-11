package sk.thenoen.yukebox.apiserver;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import sk.thenoen.yukebox.server.MediaPlayer;

public class ApiPlayController extends RouterNanoHTTPD.GeneralHandler {

	@Override
	public String getMimeType() {
		return "text/html";
	}

	@Override
	public NanoHTTPD.Response.IStatus getStatus() {
		return NanoHTTPD.Response.Status.OK;
	}

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
//		return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), text.toString());
//		return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), "{some: value}");

		MediaPlayer mediaPlayer = uriResource.initParameter(0, MediaPlayer.class);
		String videoId = session.getParameters().get("videoId").get(0);
		mediaPlayer.playVideo(videoId);
		return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), "{\"result\":\"plaing video" + videoId + "\"}");
	}

}
