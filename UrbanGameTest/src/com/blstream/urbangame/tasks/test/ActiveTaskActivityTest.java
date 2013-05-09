package com.blstream.urbangame.tasks.test;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blstream.urbangame.ActiveTaskActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.example.ExampleData;

public class ActiveTaskActivityTest extends ActivityUnitTestCase<ActiveTaskActivity> {
	
	private DatabaseInterface database;
	private Player player;
	private PlayerTaskSpecific playerTaskSpecific;
	private UrbanGame urbanGame;
	private ABCDTask abcdTask;
	
	private Context context;
	private View fragmentView;
	
	public ActiveTaskActivityTest() {
		super(ActiveTaskActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		context = getInstrumentation().getTargetContext();
		database = new Database(context);
		
		player = ExampleData.getSamplePlayer();
		playerTaskSpecific = ExampleData.getPlayerTaskSpecific();
		abcdTask = ExampleData.getSampleABCDTask();
		urbanGame = ExampleData.getSampleGame();
		
		initDB();
		initializeFragment();
	}
	
	public void initDB() {
		database.insertUser(player);
		database.setLoggedPlayer(player.getEmail());
		database.insertGameInfo(urbanGame);
		database.insertTaskForGame(urbanGame.getID(), abcdTask);
		database.insertPlayerTaskSpecific(playerTaskSpecific);
	}
	
	private void initializeFragment() {
		startActivity();
		FragmentActivity activity = getActivity();
		
		Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(
			ActiveTaskActivity.TAG_TAB_DESCRIPTION);
		fragmentView = fragment.onCreateView(activity.getLayoutInflater(),
			(ViewGroup) getActivity().findViewById(R.layout.tabhost_layout), null);
		fragment.onViewCreated(fragmentView, null);
	}
	
	private void startActivity() {
		Intent showTaskIntent = new Intent(context, ActiveTaskActivity.class);
		showTaskIntent.putExtra(ActiveTaskActivity.TASK_ID, 1L);
		startActivity(showTaskIntent, null, null);
	}
	
	/*************************************
	 ************** TESTS ****************
	 *************************************/
	
	public void testTaskTitle() {
		assertEquals(abcdTask.getTitle(), getTextViewText(R.id.textViewTaskTitle));
	}
	
	public void testTaskDescription() {
		assertEquals(abcdTask.getDescription(), getTextViewText(R.id.textViewTaskDescription));
	}
	
	public void testTaskRepeatable() {
		assertEquals(
			abcdTask.isRepetable() ? context.getString(R.string.label_taksRepeatable)
				: context.getString(R.string.string_empty), getTextViewText(R.id.textViewIsTaskRepeatable));
	}
	
	public void testTaskPoints() {
		assertEquals(playerTaskSpecific.getPoints() + context.getString(R.string.slash) + abcdTask.getMaxPoints(),
			getTextViewText(R.id.textViewTaskPoints));
	}
	
	private String getTextViewText(int textViewId) {
		return ((TextView) fragmentView.findViewById(textViewId)).getText().toString();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		context.deleteDatabase(Database.DATABASE_NAME);
		database = null;
	}
}