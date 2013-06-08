package com.blstream.urbangame.notifications.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

import com.blstream.urbangame.R;
import com.blstream.urbangame.notifications.NotificationsManager;

public class NotificationsTest extends AndroidTestCase {
	private static final String GAME_NAME = "Game name";
	private static final long GAME_ID = 1L;
	private static final String TASK_NAME = "Task name";
	private static Bitmap operatorLogo;
	
	private NotificationsManager notificationsManager;
	
	@Override
	protected void setUp() throws Exception {
		notificationsManager = new NotificationsManager(mContext);
		operatorLogo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher_big);
	}
	
	public void testNotificationGameNew() {
		notificationsManager.notifyGameNew(GAME_NAME, operatorLogo, GAME_ID);
	}
	
	public void testNotificationGameChanged() {
		notificationsManager.notifyGameChanged(GAME_NAME, operatorLogo, GAME_ID);
	}
	
	public void testNotificationGameOver() {
		notificationsManager.notifyGameOver(GAME_NAME, operatorLogo, GAME_ID);
	}
	
	public void testNotificationTaskNew() {
		notificationsManager.notifyTaskNew(GAME_NAME, TASK_NAME, operatorLogo, GAME_ID);
	}
	
	public void testNotificationTaskChanged() {
		notificationsManager.notifyTaskChanged(GAME_NAME, TASK_NAME, operatorLogo, GAME_ID);
	}
}