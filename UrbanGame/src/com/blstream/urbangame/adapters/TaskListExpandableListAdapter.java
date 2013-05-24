package com.blstream.urbangame.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.datastructures.ExpandableListHeader;
import com.blstream.urbangame.date.TimeLeftBuilder;
import com.blstream.urbangame.fragments.GameTasksFragment;

//Adapter for expandable list
public class TaskListExpandableListAdapter extends BaseExpandableListAdapter {
	
	private final String LIST = "List";
	
	private final LayoutInflater inflater;
	private final ArrayList<ExpandableListHeader<Task>> mExpandableListHeader;
	private final DatabaseInterface database;
	private final Context context;
	private final String playerEmail;
	private int numberOfHiddenTasks = 0;
	private int numberOfFindedHiddenTasks = 0;
	
	private class ViewHolder {
		public ImageView imageViewTaskLogo;
		public ImageView imageViewNewTaskIndicator;
		
		public TextView textViewTaskTitle;
		public TextView textViewTaskTimeLeft;
		public TextView textViewMaximalTaskPoints;
		public TextView textViewTaskRepeatable;
		public TextView textViewTaskPoints;
	}
	
	private class GroupViewHolder {
		public TextView textView;
		public String type;
	}
	
	public TaskListExpandableListAdapter(Context context, ArrayList<ExpandableListHeader<Task>> parent) {
		mExpandableListHeader = parent;
		inflater = LayoutInflater.from(context);
		this.context = context;
		database = new Database(context);
		playerEmail = database.getLoggedPlayerID();
	}
	
	@Override
	public int getGroupCount() {
		return mExpandableListHeader.size();
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return mExpandableListHeader.get(groupPosition).getArrayChildren().size();
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		return mExpandableListHeader.get(groupPosition).getTitle();
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		Task task = mExpandableListHeader.get(groupPosition).getArrayChildren().get(childPosition);
		return task;
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			final GroupViewHolder groupViewHolder = new GroupViewHolder();
			if (getGroup(groupPosition).equals(GameTasksFragment.EMPTY_TASK_HEADER)) {
				convertView = inflater.inflate(R.layout.fragment_tasks_empty_active_list, parent, false);
				groupViewHolder.type = GameTasksFragment.EMPTY_TASK_HEADER;
				groupViewHolder.textView = (TextView) convertView.findViewById(R.id.textViewEmptyActiveList);
				convertView.setTag(groupViewHolder);
			}
			else if (getGroup(groupPosition).equals(GameTasksFragment.HIDDEN_TASK_HEADER)) {
				convertView = inflater.inflate(R.layout.fragment_tasks_hidden_tasks_label, parent, false);
				groupViewHolder.type = GameTasksFragment.HIDDEN_TASK_HEADER;
				groupViewHolder.textView = (TextView) convertView.findViewById(R.id.textViewNumberOfHiddenTasks);
				convertView.setTag(groupViewHolder);
			}
			else {
				convertView = inflater.inflate(R.layout.expandable_lists_header_tasks, parent, false);
				groupViewHolder.type = LIST;
				groupViewHolder.textView = (TextView) convertView.findViewById(R.id.TextViewMyGamesHeader);
				convertView.setTag(groupViewHolder);
			}
		}
		
		GroupViewHolder groupViewHolder = (GroupViewHolder) convertView.getTag();
		if (getGroup(groupPosition).equals(GameTasksFragment.EMPTY_TASK_HEADER)) {
			if (!groupViewHolder.type.equals(GameTasksFragment.EMPTY_TASK_HEADER)) {
				convertView = inflater.inflate(R.layout.fragment_tasks_empty_active_list, parent, false);
				groupViewHolder.type = GameTasksFragment.EMPTY_TASK_HEADER;
				groupViewHolder.textView = (TextView) convertView.findViewById(R.id.textViewEmptyActiveList);
				convertView.setTag(groupViewHolder);
			}
			
			if (getChildrenCount(0) == 0) {
				groupViewHolder.textView.setVisibility(View.VISIBLE);
			}
			else {
				groupViewHolder.textView.setVisibility(View.GONE);
			}
		}
		else if (getGroup(groupPosition).equals(GameTasksFragment.HIDDEN_TASK_HEADER)) {
			if (!groupViewHolder.type.equals(GameTasksFragment.HIDDEN_TASK_HEADER)) {
				convertView = inflater.inflate(R.layout.fragment_tasks_hidden_tasks_label, parent, false);
				groupViewHolder.type = GameTasksFragment.HIDDEN_TASK_HEADER;
				groupViewHolder.textView = (TextView) convertView.findViewById(R.id.textViewNumberOfHiddenTasks);
				convertView.setTag(groupViewHolder);
			}
			
			groupViewHolder.textView.setText(Integer.toString(numberOfFindedHiddenTasks) + "/"
				+ Integer.toString(numberOfHiddenTasks));
		}
		else {
			if (!groupViewHolder.type.equals(LIST)) {
				convertView = inflater.inflate(R.layout.expandable_lists_header_tasks, parent, false);
				groupViewHolder.type = LIST;
				groupViewHolder.textView = (TextView) convertView.findViewById(R.id.TextViewMyGamesHeader);
				convertView.setTag(groupViewHolder);
			}
			
			groupViewHolder.textView.setText(getGroup(groupPosition).toString());
		}
		
		// always expanded
		ExpandableListView expandableListView = (ExpandableListView) parent;
		expandableListView.expandGroup(groupPosition);
		
		return convertView;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
		ViewGroup parent) {
		
		View row = null;
		Task task = mExpandableListHeader.get(groupPosition).getArrayChildren().get(childPosition);
		PlayerTaskSpecific playerTaskSpecific = database.getPlayerTaskSpecific(task.getId(), playerEmail);
		
		if (convertView == null) {
			row = inflater.inflate(R.layout.list_item_task, parent, false);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.imageViewTaskLogo = (ImageView) row.findViewById(R.id.imageViewTaskLogo);
			viewHolder.imageViewNewTaskIndicator = (ImageView) row.findViewById(R.id.imageViewNewTaskIndicator);
			viewHolder.textViewMaximalTaskPoints = (TextView) row.findViewById(R.id.textViewMaximalTaskPoints);
			viewHolder.textViewTaskPoints = (TextView) row.findViewById(R.id.textViewTaskPoints);
			viewHolder.textViewTaskRepeatable = (TextView) row.findViewById(R.id.textViewTaskRepeatable);
			viewHolder.textViewTaskTimeLeft = (TextView) row.findViewById(R.id.textViewTasksTimeLeft);
			viewHolder.textViewTaskTitle = (TextView) row.findViewById(R.id.textViewTaskTitle);
			
			row.setTag(viewHolder);
		}
		else {
			row = convertView;
		}
		
		ViewHolder viewHolder = (ViewHolder) row.getTag();
		
		// Set up all fields.
		viewHolder.textViewTaskTitle.setText(task.getTitle());
		if (task.getType() == Task.TASK_TYPE_ABCD) {
			viewHolder.imageViewTaskLogo.setImageResource(R.drawable.task_abcd_icon);
		}
		else {
			viewHolder.imageViewTaskLogo.setImageResource(R.drawable.task_gps_icon);
		}
		viewHolder.textViewTaskTimeLeft.setText((new TimeLeftBuilder(context.getResources(), task.getEndTime()))
			.getLeftTime());
		viewHolder.textViewMaximalTaskPoints.setText(task.getMaxPoints().toString());
		if (task.isRepetable()) {
			viewHolder.textViewTaskRepeatable.setText(context.getText(R.string.label_taksRepeatable));
		}
		else {
			viewHolder.textViewTaskRepeatable.setText(context.getText(R.string.label_taskNon_repeatable));
		}
		
		if (playerTaskSpecific == null) {
			viewHolder.imageViewNewTaskIndicator.setImageDrawable(null);
			viewHolder.textViewTaskPoints.setText("0");
		}
		else {
			if (playerTaskSpecific.getAreChanges()) {
				viewHolder.imageViewNewTaskIndicator.setImageResource(R.drawable.new_task_indicator);
			}
			else {
				viewHolder.imageViewNewTaskIndicator.setImageDrawable(null);
			}
			viewHolder.textViewTaskPoints.setText(playerTaskSpecific.getPoints().toString());
		}
		
		return row;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}
	
	public void setNumbersOfHiddenTasks(int numberOfHiddenTasks, int numberOfFindedHiddenTasks) {
		this.numberOfFindedHiddenTasks = numberOfFindedHiddenTasks;
		this.numberOfHiddenTasks = numberOfHiddenTasks;
	}
	
}
