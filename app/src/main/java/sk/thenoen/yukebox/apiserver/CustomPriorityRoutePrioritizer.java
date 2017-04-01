package sk.thenoen.yukebox.apiserver;

import java.util.Collection;
import java.util.TreeSet;

import fi.iki.elonen.router.RouterNanoHTTPD;

public class CustomPriorityRoutePrioritizer extends RouterNanoHTTPD.ProvidedPriorityRoutePrioritizer {

	@Override
	protected Collection<RouterNanoHTTPD.UriResource> newMappingCollection() {
		return new TreeSet<>();
	}

}
