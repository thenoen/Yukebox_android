package sk.thenoen.yukebox.httpserver;

import java.io.File;

import fi.iki.elonen.router.RouterNanoHTTPD;
import sk.thenoen.yukebox.httpserver.controller.SearchController;
import sk.thenoen.yukebox.httpserver.controller.ThumbnailController;

public class ApiHttpServer extends RouterNanoHTTPD {

	private File wwwDir;
	private ProvidedPriorityRoutePrioritizer providedPriorityRoutePrioritizer;

	public ApiHttpServer(int port, File wwwDir) {
		super(port);
		this.wwwDir = wwwDir;
		providedPriorityRoutePrioritizer = new CustomPriorityRoutePrioritizer();
		this.setRoutePrioritizer(providedPriorityRoutePrioritizer);
		addMappings();
	}

	//may be needed if a domain name will be used instead of ip address
	public ApiHttpServer(String hostname, int port) {
		super(hostname, port);
	}

	@Override
	public void addMappings() {
		super.addMappings();
//		this.addRoute("/api/play", MediaPlayerController.class, new Object());
		providedPriorityRoutePrioritizer.addRoute(SearchController.ROUTE_MAPPING, SearchController.ROUTE_PRIORITY, SearchController.class);
		providedPriorityRoutePrioritizer.addRoute(ThumbnailController.ROUTE_MAPPING, ThumbnailController.ROUTE_PRIORITY, ThumbnailController.class);
		providedPriorityRoutePrioritizer.addRoute("/.*", 13, StaticPageHandler.class, wwwDir);


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

	public void addMapping(String url, int priority, Class<?> handler, Object... initParameter) {
		providedPriorityRoutePrioritizer.addRoute(url, priority, handler, initParameter);
	}
}
