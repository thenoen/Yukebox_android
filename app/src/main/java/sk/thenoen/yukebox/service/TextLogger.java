package sk.thenoen.yukebox.service;

import android.os.Handler;
import android.widget.EditText;


public class TextLogger {

	private EditText textOutput;
	private Handler handler = new Handler();

	public TextLogger(EditText textOutput) {
		this.textOutput = textOutput;
	}

	public void log(final String text) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				textOutput.append(text + "\n");
			}
		});
	}
}
