package com.blstream.urbangame;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.menuitem.MenuItemHelper; 
import com.blstream.urbangame.fragments.LoginRegisterPageAdapter;

public class LoginRegisterActivity extends SherlockFragmentActivity {
  private LoginRegisterPageAdapter sectionsPagerAdapter;
  private ViewPager viewPager;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setSupportProgressBarVisibility(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setContentView(R.layout.activity_login_register);
    
    sectionsPagerAdapter = new LoginRegisterPageAdapter(getSupportFragmentManager(), LoginRegisterActivity.this);
    viewPager = (ViewPager) findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getSupportMenuInflater();
    menuInflater.inflate(R.menu.top_bar_menu_more, menu);
    MenuItemHelper.initLogoutMenuItem(this, menu);

    return true;
  }
  
  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    int itemId = item.getItemId();
    switch (itemId) {
      case android.R.id.home:
        finish();
        break;
    }
    return true;
  }
}