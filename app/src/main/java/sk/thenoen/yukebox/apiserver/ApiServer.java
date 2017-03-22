package sk.thenoen.yukebox.apiserver;

import fi.iki.elonen.router.RouterNanoHTTPD;

public class ApiServer extends RouterNanoHTTPD {
	public ApiServer(int port) {
		super(port);
		addMappings();
	}

	public ApiServer(String hostname, int port) {
		super(hostname, port);
	}

	@Override
	public void addMappings() {
		super.addMappings();
		addRoute("/api/.*", ApiController.class);
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
//		addRoute("/browse/(.)+", StaticPageTestHandler.class, new File("src/test/resources").getAbsoluteFile());
	}
}
