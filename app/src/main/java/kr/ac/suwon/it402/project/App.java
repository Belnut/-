package kr.ac.suwon.it402.project;

import android.app.Application;

import kr.ac.suwon.it402.project.core.LockManager;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		LockManager.getInstance().enableAppLock(this);
	}

}
