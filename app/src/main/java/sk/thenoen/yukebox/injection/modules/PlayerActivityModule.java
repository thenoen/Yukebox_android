package sk.thenoen.yukebox.injection.modules;


import android.app.Activity;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import sk.thenoen.yukebox.activity.PlayerActivity;
import sk.thenoen.yukebox.application.YukeboxApplication;

@Module(subcomponents = PlayerActivityModule.PlayerActivitySubcomponent.class)
public abstract class PlayerActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(PlayerActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindPlayerActivityInjectorFactory(PlayerActivitySubcomponent.Builder builder);

    @Subcomponent(modules = {DatabaseModule.class})
    @Singleton
    public interface PlayerActivitySubcomponent extends AndroidInjector<PlayerActivity> {
        @Subcomponent.Builder
        public abstract class Builder extends AndroidInjector.Builder<PlayerActivity> {

            // this method has to be public, it is required by Dagger2 code generation
            public abstract Builder databaseModule(DatabaseModule module);

            @Override
            public void seedInstance(PlayerActivity activity) {
                databaseModule(new DatabaseModule((YukeboxApplication)activity.getApplicationContext()));
            }
        }
    }
}
