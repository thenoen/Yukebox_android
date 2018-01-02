package sk.thenoen.yukebox.injection;

import javax.inject.Singleton;

import dagger.Component;
import sk.thenoen.yukebox.httpserver.controller.SearchController;
import sk.thenoen.yukebox.injection.modules.ServiceModule;

@Singleton
@Component(modules = {ServiceModule.class})
public interface YukeboxManualComponent {

	void inject(SearchController searchController);
}
