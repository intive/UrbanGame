package com.blstream.urbangame.notifications;

/**
 * Global accessible data connected with notifications
 */
public class NotificationConstans {
	// formatter:off

	//************* NOTIFICATION BEHAVIOUR *************//
	/*
	 * Defines if notification should be shown or dismissed (if dated)
	 */
	public static final String NOTIFICATION_SHOW = "NOTIFICATION_SHOW";
	public static final String NOTIFICATION_HIDE = "NOTIFICATION_HIDE";

	//*************** NOTIFICATION DATA ****************//
	/*
	 * Keys used to passing data to notifications and activities
	 */
	public static final String NOTIFICATION_GAME_NAME 		= "NOTIFICATION_GAME_NAME";
	public static final String NOTIFICATION_GAME_ID 		= "NOTIFICATION_GAME_ID";
	public static final String NOTIFICATION_TASK_NAME 		= "NOTIFICATION_TASK_NAME";
	public static final String NOTIFICATION_OPERATOR_LOGO 	= "NOTIFICATION_OPERATOR_LOGO";
	public static final String NOTIFICATION_DIFF 			= "NOTIFICATION_DIFF";

	//*************** NOTIFICATION TYPES ***************//
	/* order-type-7
		// type-1 = GAME
		// type-2 = TASK
	 * It is easier to use static final ints rather than enums
	 */
	public static final int GAME_WON 		= 0x117;
	public static final int GAME_LOST 		= 0x217;
	public static final int GAME_CHANGED 	= 0x317;
	public static final int TASK_NEW 		= 0x427;
	public static final int TASK_CHANGED 	= 0x527;
}