package sk.thenoen.yukebox.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.io.File;

import sk.thenoen.yukebox.R;
import sk.thenoen.yukebox.service.InitializationService;
import sk.thenoen.yukebox.service.LifecycleStateChecker;
import sk.thenoen.yukebox.service.TextLogger;

public class InitActivity extends AppCompatActivity {

	private EditText logOutputText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean initFromLIfe = InitActivity.class.isAssignableFrom(LifecycleOwner.class);
		boolean initFromSupport = InitActivity.class.isAssignableFrom(AppCompatActivity.class);
		boolean objectFromString = Object.class.isAssignableFrom(String.class);
		boolean stringFromObject = String.class.isAssignableFrom(Object.class);

//		String s = (String) new Object();
//		Object o = (Object) new String();

		AppCompatActivity a = (AppCompatActivity) this;
		InitActivity i = (InitActivity) this;



		LifecycleStateChecker lifecycleStateChecker = new LifecycleStateChecker();
		this.getLifecycle().addObserver(lifecycleStateChecker);


		setContentView(R.layout.activity_init);

		logOutputText = (EditText) findViewById(R.id.init_logging_text);

		TextLogger textLogger = new TextLogger(logOutputText);
		textLogger.log("Init started ...");

		AssetManager assetManager = getAssets();
		File applicationFilesRootFolder = getFilesDir();
		String targetDirName = getResources().getString(R.string.www_dir_name);
		final InitializationService initializationService = new InitializationService(assetManager, applicationFilesRootFolder, targetDirName, textLogger);

		Intent intent = new Intent(this, PlayerActivity.class);

		@SuppressLint("StaticFieldLeak")
		AsyncTask<Void, Void, Void> at = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void[] objects) {
				initializationService.copyAssets();
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
				finish();
				startActivity(intent);
			}
		};

		Void[] voids = new Void[0];
		at.execute(voids);
	}
}
