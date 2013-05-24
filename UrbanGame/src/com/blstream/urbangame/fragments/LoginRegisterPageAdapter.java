package com.blstream.urbangame.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Pair;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.R;

/**
 * FragmentPagerAdapter is used to switch fragments in ViewPager
 */
public class LoginRegisterPageAdapter extends FragmentPagerAdapter {
	private FragmentsContainer fragmentsContainer;
	
	public LoginRegisterPageAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.fragmentsContainer = new FragmentsContainer(context);
	}
	
	@Override
	public SherlockFragment getItem(int position) {
		return fragmentsContainer.get(position).first;
	}
	
	@Override
	public int getCount() {
		return fragmentsContainer.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return fragmentsContainer.get(position).second;
	}
	
	/**
	 * Container class for storing fragments with titles to display in
	 * PagerTitleStrip to make PageAdapter independent of storing data.
	 * 
	 */
	private class FragmentsContainer extends ArrayList<Pair<SherlockFragment, String>> {
		private static final long serialVersionUID = 1L;
		private Context context;
		
		public FragmentsContainer(Context context) {
			this.context = context;
			/*
			 * Manually add new fragments when are needed
			 */
			addFragmentWithTitle(new LoginFragment(), R.string.tabTitle_login);
			addFragmentWithTitle(new RegisterFragment(), R.string.tabTitle_register);
		}
		
		private void addFragmentWithTitle(SherlockFragment fragment, int fragmentTitleResourceID) {
			String title = context.getString(fragmentTitleResourceID);
			add(new Pair<SherlockFragment, String>(fragment, title));
		}
	}
}