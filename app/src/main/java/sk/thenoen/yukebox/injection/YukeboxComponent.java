package sk.thenoen.yukebox.injection;


import dagger.Component;
import dagger.android.AndroidInjectionModule;
import sk.thenoen.yukebox.application.YukeboxApplication;
import sk.thenoen.yukebox.injection.modules.PlayerActivityModule;

@Component(modules = {AndroidInjectionModule.class, PlayerActivityModule.class})
public interface YukeboxComponent {

//    @Component.Builder
//    interface Builder {
//
//        @BindsInstance
//        Builder application(YukeboxApplication yukeboxApplication);
//
//        @BindsInstance
//        Builder xdatabaseModule(DatabaseModule xdatabaseModule);
//
//        YukeboxComponent build();
//    }


    void inject(YukeboxApplication yukeboxApplication);
}
