package sk.thenoen.yukebox.application;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import sk.thenoen.yukebox.injection.DaggerYukeboxComponent;


public class YukeboxApplication extends Application implements HasActivityInjector {

    private static YukeboxApplication yukeboxApplication;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerYukeboxComponent.create().inject(this);
        yukeboxApplication = this;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    public static YukeboxApplication getInstance() {
        if (yukeboxApplication == null) {
            throw new RuntimeException("Application was not initialized");
        }
        return yukeboxApplication;
    }
}
