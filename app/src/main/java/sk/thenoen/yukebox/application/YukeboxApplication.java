package sk.thenoen.yukebox.application;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import sk.thenoen.yukebox.injection.DaggerYukeboxComponent;


public class YukeboxApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerYukeboxComponent.create().inject(this); // this works
//        DaggerYukeboxComponent.builder().xdatabaseModule(new DatabaseModule(this)).build().inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
