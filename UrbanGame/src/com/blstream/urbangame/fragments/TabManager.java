package com.blstream.urbangame.fragments;

import java.util.HashMap;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

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
public class TabManager implements TabHost.OnTabChangeListener {
	private final FragmentManager fragmentManager;
	private final FragmentActivity activity;
	private final TabHost tabHost;
	private final int containerId;
	private final HashMap<String, TabInfo> tabsMap = new HashMap<String, TabInfo>();
	private TabInfo lastTabInfo;
	
	public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
		this.activity = activity;
		this.tabHost = tabHost;
		this.containerId = containerId;
		this.fragmentManager = activity.getSupportFragmentManager();
		tabHost.setOnTabChangedListener(this);
	}
	
	static final class TabInfo {
		private final String tag;
		private final Class<?> clss;
		private Fragment fragment;
		
		TabInfo(String tag, Class<?> clss) {
			this.tag = tag;
			this.clss = clss;
		}
	}
	
	static class DummyTabFactory implements TabHost.TabContentFactory {
		private final Context context;
		
		public DummyTabFactory(Context context) {
			this.context = context;
		}
		
		@Override
		public View createTabContent(String tag) {
			View v = new View(context);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}
	
	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss) {
		tabSpec.setContent(new DummyTabFactory(activity));
		String tag = tabSpec.getTag();
		TabInfo newTabInfo = new TabInfo(tag, clss);
		
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
		tabHost.addTab(tabSpec);
	}
	
	@Override
	public void onTabChanged(String tabId) {
		TabInfo newTabInfo = tabsMap.get(tabId);
		if (lastTabInfo != newTabInfo) { // if tab is being switched
			FragmentTransaction ft = fragmentManager.beginTransaction();
			if (lastTabInfo != null) {
				if (lastTabInfo.fragment != null) {
					ft.detach(lastTabInfo.fragment); // detach fragment if has been shown
				}
			}
			if (newTabInfo != null) {
				if (newTabInfo.fragment == null) {
					newTabInfo.fragment = Fragment.instantiate(activity, newTabInfo.clss.getName(), null);
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
}