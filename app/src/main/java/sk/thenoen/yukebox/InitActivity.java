package sk.thenoen.yukebox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import sk.thenoen.yukebox.server.LoggingActivity;

public class InitActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);

		EditText editText = (EditText) findViewById(R.id.init_logging_text);

		editText.append("Init started ...\n");

		List<String> filesToCopy = new ArrayList<>();

		filesToCopy.addAll(getResourcesToCopy());

		File wwwDirectory = new File(getFilesDir(), getResources().getString(R.string.www_dir_name));
		if (!wwwDirectory.exists()) {
			wwwDirectory.mkdirs();
		}


		for (String s : filesToCopy) {
			editText.append(s + "\n");
			copyAssetToInternalStorage(s);
		}

		Intent intent = new Intent(this, LoggingActivity.class);
		startActivity(intent);
	}

	private void copyAssetToInternalStorage(String s) {
		try {
			InputStream inputStream = getAssets().open(s);
			String outFilePath = getFilesDir().getAbsolutePath() + File.separator + s;
			File outFile = new File(outFilePath);
			outFile.getParentFile().mkdirs();
			outFile.createNewFile();
			OutputStream outputStream = new FileOutputStream(outFile);
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> getResourcesToCopy() {
		List<String> filesToCopy = new ArrayList<>();

		try {
			for (String s : getFiles(getResources().getString(R.string.www_dir_name))) {
				filesToCopy.add(getResources().getString(R.string.www_dir_name) + File.separator + s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filesToCopy;
	}

	private List<String> getFiles(String dir) throws IOException {
		List<String> files = new ArrayList<>();
		for (String s : getAssets().list(dir)) {
			if (getAssets().list(dir + File.separator + s).length == 0) {
				files.add(s);
			} else {
				for (String file : getFiles(dir + File.separator + s)) {
					files.add(s + File.separator + file);
				}
			}
		}
		return files;
	}

}
