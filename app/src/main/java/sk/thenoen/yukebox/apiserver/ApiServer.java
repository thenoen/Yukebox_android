package sk.thenoen.yukebox.apiserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fi.iki.elonen.router.RouterNanoHTTPD;

public class ApiServer extends RouterNanoHTTPD {

	private File wwwDir;
	private ProvidedPriorityRoutePrioritizer providedPriorityRoutePrioritizer;

	public ApiServer(int port, File wwwDir) {
		super(port);
		this.wwwDir = wwwDir;
		providedPriorityRoutePrioritizer = new CustomPriorityRoutePrioritizer();
		this.setRoutePrioritizer(providedPriorityRoutePrioritizer);
		addMappings();
	}

	public ApiServer(String hostname, int port) {
		super(hostname, port);
	}

	@Override
	public void addMappings() {
		super.addMappings();
//		this.addRoute("/api/play", ApiPlayController.class, new Object());
		providedPriorityRoutePrioritizer.addRoute("/api/search", 11, ApiController.class);
		providedPriorityRoutePrioritizer.addRoute("/.*", 12, StaticPageHandler.class, wwwDir);


//		addRoute("/user", UserHandler.class); // add it twice to execute the
		// priority == priority case
//		addRoute("/user/help", GeneralHandler.class);
//		addRoute("/user/:id", UserHandler.class);
//		addRoute("/general/:param1/:param2", GeneralHandler.class);
//		addRoute("/photos/:customer_id/:photo_id", null);
//		addRoute("/test", String.class);
//		addRoute("/interface", UriResponder.class); // this will cause an error
		// when called
//		addRoute("/toBeDeleted", String.class);
//		removeRoute("/toBeDeleted");
//		addRoute("/stream", StreamUrl.class);
	}

	public void addMapping(String url, Class<?> handler, Object... initParameter) {
//		this.addRoute(url, handler, initParameter);
		providedPriorityRoutePrioritizer.addRoute(url, 9, handler, initParameter);
	}
}
