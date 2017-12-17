package sk.thenoen.yukebox.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import sk.thenoen.yukebox.database.model.Video;

@Dao
public interface VideoDao {

	@Query("SELECT * FROM video")
	List<Video> getAll();
}
