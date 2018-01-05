package sk.thenoen.yukebox.service;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class LifecycleStateChecker implements LifecycleObserver {


	// there is a bug in ClassesInfoCache on line 133
	// !params[0].isAssignableFrom(LifecycleOwner.class)
	// AppCompatActivity.isAssignableFrom(LifecycleOwner.class) will be always false, it should be otherwise
	// LifecycleOwner.class.isAssignableFrom(AppCompatActivity.class)
	@OnLifecycleEvent(Lifecycle.Event.ON_ANY)
	public void onAnyLifecycleEvent(Object activity, Object event) {
		logAnyEventInfo((AppCompatActivity) activity, (Lifecycle.Event) event);
	}


	@OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
	public void onCREATE(Object activity) {
		logEventInfo((AppCompatActivity) activity, Lifecycle.Event.ON_CREATE);
	}


	@OnLifecycleEvent(Lifecycle.Event.ON_START)
	public void onSTART(Object activity) {
		logEventInfo((AppCompatActivity) activity, Lifecycle.Event.ON_START);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
	public void onRESUME(Object activity) {
		logEventInfo((AppCompatActivity) activity, Lifecycle.Event.ON_RESUME);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
	public void onPAUSE(Object activity) {
		logEventInfo((AppCompatActivity) activity, Lifecycle.Event.ON_PAUSE);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
	public void onSTOP(Object activity) {
		logEventInfo((AppCompatActivity) activity, Lifecycle.Event.ON_STOP);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
	public void onDESTROY(Object activity) {
		logEventInfo((AppCompatActivity) activity, Lifecycle.Event.ON_DESTROY);
	}

	private void logAnyEventInfo(AppCompatActivity activity, Lifecycle.Event event) {
		boolean isGuiThread = Looper.getMainLooper().getThread() == Thread.currentThread();
		String activityState = activity.getLifecycle().getCurrentState().name();
		Log.i(this.getClass().getSimpleName(), "GUI-Thread:" + isGuiThread + " state:" + activityState + " event:" + event.name());

		Looper.myLooper();
	}

	private void logEventInfo(AppCompatActivity activity, Lifecycle.Event event) {
		boolean isGuiThread = Looper.getMainLooper().getThread() == Thread.currentThread();
		String activityState = activity.getLifecycle().getCurrentState().name();
		Log.i(this.getClass().getSimpleName() + "*", "GUI-Thread:" + isGuiThread + " state:" + activityState + " event:" + event.name());

		Looper.myLooper();
	}

}
