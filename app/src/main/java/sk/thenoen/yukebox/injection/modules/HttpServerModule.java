package sk.thenoen.yukebox.injection.modules;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sk.thenoen.yukebox.R;
import sk.thenoen.yukebox.application.YukeboxApplication;
import sk.thenoen.yukebox.httpserver.ApiHttpServer;
import sk.thenoen.yukebox.service.YoutubeApiService;

@Module
public class HttpServerModule {

	private static final int SERVER_PORT = 9090;

	private YukeboxApplication yukeboxApplication;

	@Inject
	YoutubeApiService youtubeApiService;

	HttpServerModule(YukeboxApplication yukeboxApplication) {
		this.yukeboxApplication = yukeboxApplication;
	}

	@Provides
	@Singleton
	ApiHttpServer provideApiHttpServer(YoutubeApiService youtubeApiService, @Named("wwwDirectory") File wwwDirectory) {
		return new ApiHttpServer(youtubeApiService, SERVER_PORT, wwwDirectory);
	}

	@Provides
	@Singleton
	@Named("wwwDirectory")
	File provideWwwDir() {
		return new File(yukeboxApplication.getFilesDir(), yukeboxApplication.getResources().getString(R.string.www_dir_name));
	}

	@Provides
	@Named("serverPort")
	public int provideServerPort() {
		return SERVER_PORT;
	}

}
