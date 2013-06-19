package com.blstream.urbangame.fragments;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * This is a helper class that implements a generic mechanism for associating
 * fragments with the tabs in a tab host. It relies on a trick. Normally a tab
 * host has a simple API for supplying a View or Intent that each tab will show.
 * This is not sufficient for switching between fragments. So instead we make
 * the content part of the tab host 0dp high (it is not shown) and the
 * TabManager supplies its own dummy view to show as the tab content. It listens
 * to changes in tabs, and takes care of switch to the correct fragment shown in
 * a separate content area whenever the selected tab changes.
 */
public class TabManager implements TabListener { //TabHost.OnTabChangeListener, 
	private final FragmentManager fragmentManager;
	private final SherlockFragmentActivity activity;
	private final ActionBar actionBar;
	private final int containerId;
	private final HashMap<String, TabInfo> tabsMap = new HashMap<String, TabInfo>();
	private TabInfo lastTabInfo;
	
	public TabManager(SherlockFragmentActivity activity, int containerId) {
		this.activity = activity;
		this.containerId = containerId;
		this.fragmentManager = activity.getSupportFragmentManager();
		actionBar = activity.getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}
	
	static final class TabInfo {
		private final String tag;
		private final Class<?> clss;
		private final Bundle args;
		private Fragment fragment;
		
		TabInfo(String tag, Class<?> clss, Bundle args) {
			this.tag = tag;
			this.clss = clss;
			this.args = args;
		}
	}
	
	public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
		String tag = (String) tab.getTag();
		tab.setTabListener(this);
		
		TabInfo newTabInfo = new TabInfo(tag, clss, args);
		
		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state.  If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		newTabInfo.fragment = fragmentManager.findFragmentByTag(tag);
		if (newTabInfo.fragment != null && !newTabInfo.fragment.isDetached()) {
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.detach(newTabInfo.fragment);
			ft.commit();
		}
		
		tabsMap.put(tag, newTabInfo);
		actionBar.addTab(tab);
	}
	
	public ActionBar.Tab prepareTab(String tag, String text) {
		ActionBar.Tab tab = actionBar.newTab();
		tab.setText(text);
		tab.setTag(tag);
		return tab;
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ftOther) {
		String tag = (String) tab.getTag();
		TabInfo newTabInfo = tabsMap.get(tag);
		
		if (lastTabInfo != newTabInfo) { // if tab has been switched
			FragmentTransaction ft = fragmentManager.beginTransaction();
			
			if (lastTabInfo != null) {
				if (lastTabInfo.fragment != null) {
					ft.detach(lastTabInfo.fragment); // detach fragment if has been shown
				}
			}
			if (newTabInfo != null) {
				if (newTabInfo.fragment == null) {
					newTabInfo.fragment = Fragment.instantiate(activity, newTabInfo.clss.getName(), newTabInfo.args);
					ft.add(containerId, newTabInfo.fragment, newTabInfo.tag);
				}
				else {
					ft.attach(newTabInfo.fragment);
				}
			}
			
			lastTabInfo = newTabInfo;
			ft.commit();
			fragmentManager.executePendingTransactions();
		}
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}
}