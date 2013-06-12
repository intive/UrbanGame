package com.blstream.urbangame.adapters;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.date.TimeLeftBuilder;

public class GamesListAdapter extends BaseExpandableListAdapter {//ArrayAdapter<UrbanGameShortInfo> {

	private ArrayList<UrbanGameShortInfo> data;
	private int viewResourceId;
	private Context context;
	private DatabaseInterface database = null;
	
	private static class ViewHolder {
		ImageView imageViewGameLogo;
		ImageView imageViewGamePrize;
		ImageView imageViewOperatorLogo;
		
		TextView textViewGameName;
		TextView textViewOperatorName;
		TextView textViewNumberOfTotalPlayers;
		TextView textViewNumberOfCurrentPlayers;
		TextView textViewLocation;
		TextView textViewStartTime;
		TextView textViewDivider;
	}
	
	public GamesListAdapter(Context context, int viewResourceId) {
		init(context, viewResourceId, null, null);
	}
	
	public GamesListAdapter(Context context, int viewResourceId, DatabaseInterface interfaceDB) {
		init(context, viewResourceId, null, interfaceDB);
	}
	
	public GamesListAdapter(Context context, int viewResourceId, ArrayList<UrbanGameShortInfo> data) {
		init(context, viewResourceId, data, null);
	}
	
	private void init(Context context, int viewResourceId, ArrayList<UrbanGameShortInfo> data,
		DatabaseInterface interfaceDB) {
		this.context = context;
		this.viewResourceId = viewResourceId;
		
		if (interfaceDB == null) {
			database = new Database(context);
		}
		else {
			database = interfaceDB;
		}
		
		if (data == null) {
			// Retrieve data from database
			this.data = (ArrayList<UrbanGameShortInfo>) database.getAllGamesShortInfo();
		}
		else {
			this.data = data;
		}
		database.closeDatabase();
		
	}
	
	private String timeLeft(Date startDate, Date endDate) {
		
		String timeString = null;
		Date currentDate = new Date();
		if (currentDate.before(startDate)) {
			timeString = startDate.toString();
		}
		else {
			TimeLeftBuilder timeLeftBuilder = new TimeLeftBuilder(context.getResources(), endDate);
			timeString = timeLeftBuilder.getLeftTime();
		}
		return timeString;
	}
	
	/**
	 * Used to update games data from database.
	 */
	public void updateData() {
		if (database == null) {
			database = new Database(context);
		}
		data = (ArrayList<UrbanGameShortInfo>) database.getAllGamesShortInfo();
		notifyDataSetChanged();
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return data.get(childPosition);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
		ViewGroup parent) {
		View row = null;
		UrbanGameShortInfo gameInfo = data.get(childPosition);
		
		if (convertView == null) {
			//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LayoutInflater inflater = LayoutInflater.from(context);
			row = inflater.inflate(viewResourceId, parent, false);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.imageViewGameLogo = (ImageView) row.findViewById(R.id.imageViewGameLogo);
			viewHolder.imageViewGamePrize = (ImageView) row.findViewById(R.id.imageViewGamePrize);
			viewHolder.imageViewOperatorLogo = (ImageView) row.findViewById(R.id.imageViewOperatorLogo);
			viewHolder.textViewGameName = (TextView) row.findViewById(R.id.textViewGameName);
			viewHolder.textViewLocation = (TextView) row.findViewById(R.id.textViewLocation);
			viewHolder.textViewNumberOfCurrentPlayers = (TextView) row
				.findViewById(R.id.textViewNumberOfCurrentPlayers);
			viewHolder.textViewNumberOfTotalPlayers = (TextView) row.findViewById(R.id.textViewNumberOfTotalPlayers);
			viewHolder.textViewOperatorName = (TextView) row.findViewById(R.id.textViewOperatorName);
			viewHolder.textViewStartTime = (TextView) row.findViewById(R.id.textViewStartTime);
			viewHolder.textViewDivider = (TextView) row.findViewById(R.id.textViewDivider);
			
			row.setTag(viewHolder);
			
		}
		else {
			row = convertView;
		}
		
		ViewHolder holder = (ViewHolder) row.getTag();
		
		// Set up all fields
		holder.imageViewGameLogo.setImageDrawable(gameInfo.getGameLogoDrawable(context.getResources()));
		if (gameInfo.getReward()) {
			holder.imageViewGamePrize.setImageResource(R.drawable.prize);
		}
		else {
			holder.imageViewGamePrize.setImageDrawable(null);
		}
		holder.imageViewOperatorLogo.setImageDrawable(gameInfo.getOperatorLogoDrawable(context.getResources()));
		holder.textViewGameName.setText(gameInfo.getTitle());
		holder.textViewLocation.setText(gameInfo.getLocation());
		holder.textViewNumberOfCurrentPlayers.setText(gameInfo.getPlayers().toString());
		if (gameInfo.getMaxPlayers() != null && gameInfo.getMaxPlayers() > 0) {
			// If the game has limit for maximum number of players.
			holder.textViewNumberOfTotalPlayers.setText(gameInfo.getMaxPlayers().toString());
			holder.textViewDivider.setText("/");
		}
		else {
			//Removing divider if the game doesn't have limit for maximum number of players.
			holder.textViewNumberOfTotalPlayers.setText("");
			holder.textViewDivider.setText("");
		}
		holder.textViewOperatorName.setText(gameInfo.getOperatorName());
		holder.textViewStartTime.setText(timeLeft(gameInfo.getStartDate(), gameInfo.getEndDate()));
		
		return row;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return data.size();
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		return data;
	}
	
	@Override
	public int getGroupCount() {
		return 1;
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.expandable_lists_header, parent, false);
		TextView groupTitle = (TextView) convertView.findViewById(R.id.TextViewMyGamesHeader);
		groupTitle.setText(R.string.header_nearby_games);
		return convertView;
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
}
