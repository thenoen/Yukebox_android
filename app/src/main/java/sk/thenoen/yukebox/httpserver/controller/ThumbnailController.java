package sk.thenoen.yukebox.httpserver.controller;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

public class ThumbnailController extends RouterNanoHTTPD.GeneralHandler {

	public static final String ROUTE_MAPPING = "/api/thumbnail";
	public static final int ROUTE_PRIORITY = 12;

	private static final Object lock = new Object();
	private static final HashSet<String> availableThumbnails = new HashSet<>();

	@Override
	public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

		String videoId = session.getParameters().get("query").get(0);

		String jsongResponse = getResponse(videoId);
		return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), jsongResponse);
	}

	private String getResponse(String s) {
		String response = null;
		response = downloadIfNotAvailable(s);
		response += loadFromDisk(s);
		return response;
	}

	private String downloadIfNotAvailable(String s) {
		synchronized (lock) {
			if (!availableThumbnails.contains(s)) {
				loadFromNetwork(s);
				return "----";
			} else {
				return "";
			}
		}
	}

	@NonNull
	private String loadFromDisk(String s) {
		return Thread.currentThread().getName() + "\n";
	}

	@NonNull
	private String loadFromNetwork(String s) {
		sleep();
		availableThumbnails.add(s);
		return "---------" + Thread.currentThread().getName() + "\n";
	}

	private void sleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
