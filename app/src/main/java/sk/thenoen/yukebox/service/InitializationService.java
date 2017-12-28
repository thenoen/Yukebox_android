package sk.thenoen.yukebox.service;

import android.content.res.AssetManager;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class InitializationService {

	private AssetManager assetManager;
	private File rootFolder;
	private String targetFolderPath;
	private TextLogger textLogger;

	public InitializationService(AssetManager assetManager, File rootFolder, String targetFolderPath, TextLogger textLogger) {
		this.assetManager = assetManager;
		this.rootFolder = rootFolder;
		this.targetFolderPath = targetFolderPath;
		this.textLogger = textLogger;
	}

	public void copyAssets() {
		List<String> filesToCopy = new ArrayList<>();

		filesToCopy.addAll(getAssetsToCopy());

		File wwwDirectory = new File(rootFolder, targetFolderPath);
		if (!wwwDirectory.exists()) {
			wwwDirectory.mkdirs();
		}


		for (String filePath : filesToCopy) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			textLogger.log(filePath);
			copyAssetToInternalStorage(filePath);
		}
	}

	private void copyAssetToInternalStorage(String s) {
		try {
			InputStream inputStream = assetManager.open(s);
			String outFilePath = rootFolder.getAbsolutePath() + File.separator + s;
			File outFile = new File(outFilePath);
			outFile.getParentFile().mkdirs();
			outFile.createNewFile();
			OutputStream outputStream = new FileOutputStream(outFile);
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> getAssetsToCopy() {
		List<String> filesToCopy = new ArrayList<>();

		try {
			for (String s : getFiles(targetFolderPath)) {
				filesToCopy.add(targetFolderPath + File.separator + s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filesToCopy;
	}

	private List<String> getFiles(String dir) throws IOException {
		List<String> files = new ArrayList<>();
		for (String s : assetManager.list(dir)) {
			if (assetManager.list(dir + File.separator + s).length == 0) {
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
