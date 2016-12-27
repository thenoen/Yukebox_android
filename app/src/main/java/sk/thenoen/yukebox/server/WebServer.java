package sk.thenoen.yukebox.server;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;


public class WebServer extends NanoHTTPD {
	public WebServer(int port) {
		super(port);
	}


	@Override
	public Response serve(IHTTPSession session) {
		Method method = session.getMethod();
		String uri = session.getUri();

		String msg = "<html><body><h1>Hello server</h1>\n";
		Map<String, String> parms = session.getParms();
		if (parms.get("username") == null) {
			msg += "<form action='?' method='get'>\n" + "  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
		} else {
			msg += "<p>Hello, " + parms.get("username") + "!</p>";
		}

		msg += "</body></html>\n";

		return NanoHTTPD.newFixedLengthResponse(msg);
	}
}
