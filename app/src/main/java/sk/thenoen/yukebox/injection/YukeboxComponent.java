package sk.thenoen.yukebox.injection;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import sk.thenoen.yukebox.application.YukeboxApplication;
import sk.thenoen.yukebox.injection.modules.PlayerActivityModule;

@Component(modules = {AndroidInjectionModule.class, PlayerActivityModule.class})
public interface YukeboxComponent {

	void inject(YukeboxApplication yukeboxApplication);
}
