package sk.thenoen.yukebox.injection.modules;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.google.common.io.BaseEncoding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sk.thenoen.yukebox.application.YukeboxApplication;
import sk.thenoen.yukebox.service.YoutubeApiService;

@Module
public class ServiceModule {

	private YoutubeApiService youtubeApiService;

	@Inject
	ServiceModule(YukeboxApplication yukeboxApplication) {
		String packageName = yukeboxApplication.getPackageName();
		String sha1 = getSHA1(packageName, yukeboxApplication);
		youtubeApiService = YoutubeApiService.getInstance(packageName, sha1);
	}

	@Provides
	@Singleton
	YoutubeApiService providesYoutubeService() {
		return youtubeApiService;
	}

	private String getSHA1(String packageName, Application application){
		try {
			Signature[] signatures = application.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
			for (Signature signature: signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA-1");
				md.update(signature.toByteArray());
				return BaseEncoding.base16().encode(md.digest());
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
