package com.blstream.urbangame;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;

public class GamesListAdapter extends ArrayAdapter<UrbanGameShortInfo> {
	
	private ArrayList<UrbanGameShortInfo> data;
	private final int viewResourceId;
	private final Context context;
	private DatabaseInterface databaseInterface = null;
	
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
		super(context, viewResourceId);
		this.viewResourceId = viewResourceId;
		this.context = context;
		data = new ArrayList<UrbanGameShortInfo>();
	}
	
	public GamesListAdapter(Context context, int viewResourceId, DatabaseInterface interfaceDB) {
		super(context, viewResourceId);
		this.viewResourceId = viewResourceId;
		this.context = context;
		databaseInterface = interfaceDB;
		
		// Retrieve data from database
		data = (ArrayList<UrbanGameShortInfo>) databaseInterface.getAllGamesShortInfo();
	}
	
	public GamesListAdapter(Context context, int viewResourceId, ArrayList<UrbanGameShortInfo> data) {
		super(context, viewResourceId);
		this.context = context;
		this.viewResourceId = viewResourceId;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = null;
		UrbanGameShortInfo gameInfo = data.get(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		holder.imageViewOperatorLogo.setImageDrawable(gameInfo.getOperatorLogoDrawable(context.getResources()));
		holder.textViewGameName.setText(gameInfo.getTitle());
		holder.textViewLocation.setText(gameInfo.getLocation());
		holder.textViewNumberOfCurrentPlayers.setText(gameInfo.getPlayers().toString());
		if (gameInfo.getMaxPlayers() != null) {
			holder.textViewNumberOfTotalPlayers.setText(gameInfo.getMaxPlayers().toString());
			holder.textViewDivider.setText("/");
		}
		else {
			//Removing divider
			holder.textViewDivider.setText("");
		}
		holder.textViewOperatorName.setText(gameInfo.getOperatorName());
		holder.textViewStartTime.setText(TimeLeft.timeLeft(gameInfo.getStartDate(), gameInfo.getEndDate(), new Date(),
			context));
		
		return row;
	}
	
	public void setDatabaseInterface(DatabaseInterface interfaceDB) {
		databaseInterface = interfaceDB;
		data = (ArrayList<UrbanGameShortInfo>) databaseInterface.getAllGamesShortInfo();
	}
	
	public DatabaseInterface getDatabaseInterface() {
		return databaseInterface;
	}
	
	public void updateData() {
		if (databaseInterface != null) {
			data = (ArrayList<UrbanGameShortInfo>) databaseInterface.getAllGamesShortInfo();
			notifyDataSetChanged();
		}
	}
	
}
