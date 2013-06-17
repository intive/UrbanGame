package com.blstream.urbangame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

public abstract class AbstractGamesList extends AbstractMenuActivity implements OnNavigationListener {
	private ArrayAdapter<CharSequence> spinnerAdapter;
	
	@Override
	protected void onResume() {
		super.onResume();
		this.supportInvalidateOptionsMenu();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (isUserLoggedIn()) {
			configureUserGamesFlow();
		}
		else {
			configureUserLoginFlow(menu);
		}
		
		return super.onCreateOptionsMenu(menu);
	}
	
	protected void configureUserGamesFlow() {
		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> spinnerAdapter = createAdapter(context);
		configureActionBar(spinnerAdapter);
	}
	
	private ArrayAdapter<CharSequence> createAdapter(Context context) {
		spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.menu_navigation_list,
			R.layout.sherlock_spinner_item);
		spinnerAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		return spinnerAdapter;
	}
	
	private void configureActionBar(ArrayAdapter<CharSequence> spinnerAdapter) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(spinnerAdapter, this);
		actionBar.setSelectedNavigationItem(getSelectedPosition());
		actionBar.setDisplayShowTitleEnabled(false);
	}
	
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Class<? extends Activity> cls = getClassFromPosition(itemPosition);
		startActivityIfClassWillChange(cls);
		return false;
	}
	
	private Class<? extends Activity> getClassFromPosition(int position) {
		if (position == 0) return MyGamesActivity.class;
		if (position == 1) return GamesListActivity.class;
		return null;
	}
	
	private void startActivityIfClassWillChange(Class<? extends Activity> cls) {
		if (classWillChange(cls)) {
			startActivity(new Intent(this, cls));
			finish();
		}
	}
	
	private int getSelectedPosition() {
		return getPositionFromClass(getClass());
	}
	
	private int getPositionFromClass(Class<? extends Activity> cls) {
		if (cls.equals(MyGamesActivity.class)) return 0;
		if (cls.equals(GamesListActivity.class)) return 1;
		return -1;
	}
	
	private boolean classWillChange(Class<? extends Activity> cls) {
		return cls != null && !cls.equals(getClass());
	}
	
	protected void configureUserLoginFlow(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_login, menu);
		configureLoginAction(menu);
	}
	
	private void configureLoginAction(Menu menu) {
		final MenuItem loginItem = menu.findItem(R.id.menu_login);
		loginItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				boolean isUserLoggedIn = isUserLoggedIn();
				startActivityDependOnUserState(isUserLoggedIn);
				return true;
			}
			
			private void startActivityDependOnUserState(boolean isUserLoggedIn) {
				Intent intent = getActivityDependOnUserState(isUserLoggedIn);
				startActivity(intent);
				finish();
			}
			
			private Intent getActivityDependOnUserState(boolean isUserLoggedIn) {
				Intent intent = null;
				if (isUserLoggedIn) {
					intent = getIntentFromClass(MyGamesActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				}
				else {
					intent = getIntentFromClass(LoginRegisterActivity.class);
				}
				return intent;
			}
			
			private Intent getIntentFromClass(Class<? extends Activity> cls) {
				return new Intent(AbstractGamesList.this, cls);
			}
		});
	}
}