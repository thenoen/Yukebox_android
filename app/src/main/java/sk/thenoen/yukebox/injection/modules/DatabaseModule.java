package sk.thenoen.yukebox.injection.modules;

import android.arch.persistence.room.Room;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sk.thenoen.yukebox.application.YukeboxApplication;
import sk.thenoen.yukebox.database.YukeboxDatabase;
import sk.thenoen.yukebox.database.dao.VideoDao;

@Module
public class DatabaseModule {

    private YukeboxDatabase yukeboxDatabase;

    @Inject
    public DatabaseModule(YukeboxApplication yukeboxApplication) {
        yukeboxDatabase = Room.databaseBuilder(yukeboxApplication, YukeboxDatabase.class, "YukeboxDatabase").build();
    }

    @Provides
    @Singleton //todo check memory issues/bugs
    public VideoDao providesVideoDao() {
        return yukeboxDatabase.videoDao();
    }
}
