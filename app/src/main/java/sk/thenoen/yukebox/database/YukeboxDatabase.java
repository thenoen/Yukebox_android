package sk.thenoen.yukebox.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import sk.thenoen.yukebox.database.dao.VideoDao;
import sk.thenoen.yukebox.database.model.Video;

@Database(entities = {Video.class}, version = 1)
public abstract class YukeboxDatabase extends RoomDatabase {
	public abstract VideoDao videoDao();
}
