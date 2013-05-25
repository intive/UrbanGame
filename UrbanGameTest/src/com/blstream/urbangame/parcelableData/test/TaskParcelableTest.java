/* Checks if tasks are passed properlly through intents */

package com.blstream.urbangame.parcelableData.test;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.test.AndroidTestCase;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Task;

public class TaskParcelableTest extends AndroidTestCase {
	
	private Task locationTask;
	private Task abcdTask;
	private Task locationTaskAfterPassing;
	private Task abcdTaskAfterPassing;
	private LocationTask locationTask2;
	private LocationTask locationTaskAfterPassing2;
	private ABCDTask abcdTask2;
	private ABCDTask abcdTaskAfterPassing2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		locationTask2 = new LocationTask(1L, "test title", "test picture", "test description", true, true, 4,
			new Date(), 20);
		String[] testQuestions = { "test answer 1", "test answer 2" };
		abcdTask2 = new ABCDTask(2L, "test title2", "test picture2", "test description2", false, false, 3, new Date(),
			15, "test question", testQuestions);
		
		//parcel Location task
		Intent i = new Intent();
		Bundle extras = new Bundle();
		extras.putParcelable(Task.TASK_KEY, locationTask2);
		i.putExtras(extras);
		locationTaskAfterPassing2 = (LocationTask) i.getExtras().get(Task.TASK_KEY);
		
		//parcel abcd task
		i = new Intent();
		extras = new Bundle();
		extras.putParcelable(Task.TASK_KEY, abcdTask2);
		i.putExtras(extras);
		abcdTaskAfterPassing2 = (ABCDTask) i.getExtras().get(Task.TASK_KEY);
		
		//parcel location task as Task
		locationTask = locationTask2;
		i = new Intent();
		extras = new Bundle();
		extras.putParcelable(Task.TASK_KEY, locationTask);
		i.putExtras(extras);
		locationTaskAfterPassing = (LocationTask) i.getExtras().get(Task.TASK_KEY);
		
		//parcel abct task as Task
		abcdTask = abcdTask2;
		i = new Intent();
		extras = new Bundle();
		extras.putParcelable(Task.TASK_KEY, abcdTask);
		i.putExtras(extras);
		abcdTaskAfterPassing = (ABCDTask) i.getExtras().get(Task.TASK_KEY);
		
	}
	
	public void testLocationTaskParcelable() {
		
		assertEquals(locationTaskAfterPassing2.getId(), locationTask2.getId());
		assertEquals(locationTaskAfterPassing2.getDescription(), locationTask2.getDescription());
		assertEquals(locationTaskAfterPassing2.getPictureBase64(), locationTask2.getPictureBase64());
		assertEquals(locationTaskAfterPassing2.getTitle(), locationTask2.getTitle());
		assertEquals(locationTaskAfterPassing2.getEndTime(), locationTask2.getEndTime());
		assertEquals(locationTaskAfterPassing2.getMaxPoints(), locationTask2.getMaxPoints());
		assertEquals(locationTaskAfterPassing2.getNumberOfHidden(), locationTask2.getNumberOfHidden());
		assertEquals(locationTaskAfterPassing2.getType(), locationTask2.getType());
		
	}
	
	public void testAbcdTaskParcelable() {
		
		assertEquals(abcdTaskAfterPassing2.getId(), abcdTask2.getId());
		assertEquals(abcdTaskAfterPassing2.getDescription(), abcdTask2.getDescription());
		assertEquals(abcdTaskAfterPassing2.getPictureBase64(), abcdTask2.getPictureBase64());
		assertEquals(abcdTaskAfterPassing2.getTitle(), abcdTask2.getTitle());
		assertEquals(abcdTaskAfterPassing2.getEndTime(), abcdTask2.getEndTime());
		assertEquals(abcdTaskAfterPassing2.getMaxPoints(), abcdTask2.getMaxPoints());
		assertEquals(abcdTaskAfterPassing2.getNumberOfHidden(), abcdTask2.getNumberOfHidden());
		assertEquals(abcdTaskAfterPassing2.getType(), abcdTask2.getType());
		assertEquals(abcdTaskAfterPassing2.getQuestion(), abcdTask2.getQuestion());
		assertEquals(abcdTaskAfterPassing2.getAnswers(), abcdTask2.getAnswers());
		
	}
	
	public void testLocationTaskAsTaskParcelable() {
		
		assertEquals(locationTaskAfterPassing.getId(), locationTask.getId());
		assertEquals(locationTaskAfterPassing.getDescription(), locationTask.getDescription());
		assertEquals(locationTaskAfterPassing.getPictureBase64(), locationTask.getPictureBase64());
		assertEquals(locationTaskAfterPassing.getTitle(), locationTask.getTitle());
		assertEquals(locationTaskAfterPassing.getEndTime(), locationTask.getEndTime());
		assertEquals(locationTaskAfterPassing.getMaxPoints(), locationTask.getMaxPoints());
		assertEquals(locationTaskAfterPassing.getNumberOfHidden(), locationTask.getNumberOfHidden());
		assertEquals(locationTaskAfterPassing.getType(), locationTask.getType());
		
	}
	
	public void testAbcdTaskAsTastParcelable() {
		
		assertEquals(abcdTaskAfterPassing.getId(), abcdTask.getId());
		assertEquals(abcdTaskAfterPassing.getDescription(), abcdTask.getDescription());
		assertEquals(abcdTaskAfterPassing.getPictureBase64(), abcdTask.getPictureBase64());
		assertEquals(abcdTaskAfterPassing.getTitle(), abcdTask.getTitle());
		assertEquals(abcdTaskAfterPassing.getEndTime(), abcdTask.getEndTime());
		assertEquals(abcdTaskAfterPassing.getMaxPoints(), abcdTask.getMaxPoints());
		assertEquals(abcdTaskAfterPassing.getNumberOfHidden(), abcdTask.getNumberOfHidden());
		assertEquals(abcdTaskAfterPassing.getType(), abcdTask.getType());
		
	}
	
}
