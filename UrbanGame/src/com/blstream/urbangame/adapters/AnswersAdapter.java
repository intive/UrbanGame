package com.blstream.urbangame.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.blstream.urbangame.R;
import com.blstream.urbangame.helpers.Pair;

public class AnswersAdapter extends ArrayAdapter<Pair<String, Boolean>> {
	
	private final Context context;
	private final int viewResourceId;
	private ArrayList<String> correctAnswers = null;
	private final List<Pair<String, Boolean>> items;
	private boolean isAnswerEnabled;
	
	private class ViewHolder {
		public TextView textViewAnswer;
		public CheckBox checkBoxAnswer;
	}
	
	public AnswersAdapter(Context context, int viewResourceId, List<Pair<String, Boolean>> objects) {
		super(context, viewResourceId, objects);
		this.context = context;
		this.viewResourceId = viewResourceId;
		this.items = objects;
		this.isAnswerEnabled = true;
	}
	
	public List<Pair<String, Boolean>> getItems() {
		return items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = null;
		Pair<String, Boolean> answer = getItem(position);
		ViewHolder viewHolder;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(viewResourceId, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.textViewAnswer = (TextView) row.findViewById(R.id.textViewAnswer);
			viewHolder.checkBoxAnswer = (CheckBox) row.findViewById(R.id.checkBoxAnswer);
			viewHolder.checkBoxAnswer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int position = (Integer) buttonView.getTag();
					Pair<String, Boolean> answer = AnswersAdapter.this.getItem(position);
					answer.second = isChecked;
				}
			});
			
			row.setTag(viewHolder);
		}
		else {
			row = convertView;
			viewHolder = (ViewHolder) row.getTag();
		}
		
		viewHolder.textViewAnswer.setText(answer.first);
		viewHolder.checkBoxAnswer.setTag(Integer.valueOf(position));
		viewHolder.checkBoxAnswer.setChecked(answer.second);
		viewHolder.checkBoxAnswer.setEnabled(isAnswerEnabled);
		
		if (correctAnswers != null) {
			if (correctAnswers.contains(answer.first)) {
				viewHolder.textViewAnswer.setTextColor(context.getResources().getColor(R.color.answer_green));
			}
			else {
				viewHolder.textViewAnswer.setTextColor(context.getResources().getColor(android.R.color.black));
			}
		}
		
		return row;
	}
	
	public void setCorrectAnswers(ArrayList<String> correctAnswers) {
		this.correctAnswers = correctAnswers;
		notifyDataSetInvalidated();
	}
	
	public void setEnabled(boolean isAnswerEnabled) {
		this.isAnswerEnabled = isAnswerEnabled;
	}
}