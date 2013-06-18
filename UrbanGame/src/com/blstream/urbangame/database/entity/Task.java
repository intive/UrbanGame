package com.blstream.urbangame.database.entity;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.blstream.urbangame.database.helper.Base64ImageCoder;

public abstract class Task implements Parcelable {
	
	public static final int TASK_TYPE_ABCD = 0;
	public static final int TASK_TYPE_LOCATION = 1;
	
	public static final String TASK_KEY = "Task";
	
	private Long id;
	private Integer type;
	private String title;
	private String pictureBase64;
	private Drawable pictureDrawable;
	private String description;
	private Boolean isRepetable;
	private Boolean isHidden;
	private Integer numberOfHidden;
	private Date endTime;
	private Integer maxPoints;
	
	/** @param id
	 * @param type
	 * @param title
	 * @param pictureBase64
	 * @param description
	 * @param isRepetable
	 * @param isHidden
	 * @param numberOfHidden
	 * @param endTime
	 * @param maxPoints */
	public Task(Long id, Integer type, String title, String pictureBase64, String description, Boolean isRepetable,
		Boolean isHidden, Integer numberOfHidden, Date endTime, Integer maxPoints) {
		this.id = id;
		this.type = type;
		this.title = title;
		this.pictureBase64 = pictureBase64;
		this.description = description;
		this.isRepetable = isRepetable;
		this.isHidden = isHidden;
		this.numberOfHidden = numberOfHidden;
		this.endTime = endTime;
		this.maxPoints = maxPoints;
	}
	
	public Task() {
		this(null, null, null, null, null, null, null, null, null, null);
	}
	
	public Task(Parcel in) {
		this.id = in.readLong();
		this.type = in.readInt();
		this.title = in.readString();
		this.pictureBase64 = in.readString();
		this.description = in.readString();
		this.isRepetable = in.readByte() == 1;
		this.isHidden = in.readByte() == 1;
		this.numberOfHidden = in.readInt();
		this.endTime = (Date) in.readSerializable();
		this.maxPoints = in.readInt();
	}
	
	/** @return the id */
	public Long getId() {
		return id;
	}
	
	/** @param id the id to set */
	public void setId(Long id) {
		this.id = id;
	}
	
	/** @return the type */
	public Integer getType() {
		return type;
	}
	
	/** @param type the type to set */
	public void setType(Integer type) {
		this.type = type;
	}
	
	/** @return the title */
	public String getTitle() {
		return title;
	}
	
	/** @param title the title to set */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/** @return the pictureBase64 */
	public String getPictureBase64() {
		return pictureBase64;
	}
	
	/** @param pictureBase64 the pictureBase64 to set */
	public void setPictureBase64(String pictureBase64) {
		this.pictureBase64 = pictureBase64;
	}
	
	/** @param res - the resources obtained from getResources() [Activity method]
	 * @return the pictureDrawable */
	public Drawable getPictureDrawable(Resources res) {
		Drawable ret = null;
		if (pictureDrawable == null && pictureBase64 != null) {
			ret = Base64ImageCoder.decodeImage(pictureBase64, res);
		}
		return ret;
	}
	
	/** @deprecated - Shouldn't be used. Seting image only from server which is
	 * in base64 format
	 * @param pictureDrawable the pictureDrawable to set */
	@Deprecated
	public void setPictureDrawable(Drawable pictureDrawable) {
		this.pictureDrawable = pictureDrawable;
	}
	
	/** @return the description */
	public String getDescription() {
		return description;
	}
	
	/** @param description the description to set */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/** @return the isRepetable */
	public Boolean isRepetable() {
		return isRepetable;
	}
	
	/** @param isRepetable the isRepetable to set */
	public void setIsRepetable(Boolean isRepetable) {
		this.isRepetable = isRepetable;
	}
	
	/** @return the isHidden */
	public Boolean isHidden() {
		return isHidden;
	}
	
	/** @param isHidden the isHidden to set */
	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}
	
	/** @return the numberOfHidden */
	public Integer getNumberOfHidden() {
		return numberOfHidden;
	}
	
	/** @param numberOfHidden the numberOfHidden to set */
	public void setNumberOfHidden(Integer numberOfHidden) {
		this.numberOfHidden = numberOfHidden;
	}
	
	/** @return the endTime */
	public Date getEndTime() {
		return endTime;
	}
	
	/** @param endTime the endTime to set */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	/** @return the maxPoints */
	public Integer getMaxPoints() {
		return maxPoints;
	}
	
	/** @param maxPoints the maxPoints to set */
	public void setMaxPoints(Integer maxPoints) {
		this.maxPoints = maxPoints;
	}
	
	//parcel task
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeInt(type);
		out.writeString(title);
		out.writeString(pictureBase64);
		out.writeString(description);
		out.writeByte((byte) (isRepetable ? 1 : 0));
		out.writeByte((byte) (isHidden ? 1 : 0));
		out.writeInt(numberOfHidden);
		out.writeSerializable(endTime);
		out.writeInt(maxPoints);
	}
	
	@Override
	public boolean equals(Object object) {
		
		if (object == this) return true;
		if (object == null || object.getClass() != this.getClass()) return false;
		
		Task task = (Task) object;
		
		if (getDescription().equals(task.getDescription()) && getEndTime().equals(task.getEndTime())
			&& getId().equals(task.getId()) && getMaxPoints().equals(task.getMaxPoints())
			&& isHidden().equals(task.isHidden()) && isRepetable().equals(task.isRepetable())
			&& getNumberOfHidden().equals(task.getNumberOfHidden()) && getTitle().equals(task.getTitle())
			&& getType().equals(task.getType())) {
			if (getType() == Task.TASK_TYPE_ABCD) {
				ABCDTask thisABCDtask = (ABCDTask) this;
				ABCDTask ABCDtask = (ABCDTask) task;
				
				if (thisABCDtask.getQuestion().equals(ABCDtask.getQuestion())
					&& Arrays.equals(thisABCDtask.getAnswers(), ABCDtask.getAnswers())) return true;
				else return false;
			}
			else return true;
		}
		
		return false;
		
	}
	
	@Override
	public int hashCode() {
		
		HashCodeBuilder builder = new HashCodeBuilder();
		
		builder.append(getDescription());
		builder.append(getEndTime());
		builder.append(getId());
		builder.append(isHidden());
		builder.append(isRepetable());
		builder.append(getMaxPoints());
		builder.append(getNumberOfHidden());
		builder.append(getTitle());
		
		if (getType() == Task.TASK_TYPE_ABCD) {
			ABCDTask thisABCDtask = (ABCDTask) this;
			builder.append(thisABCDtask.getQuestion());
			builder.append(thisABCDtask.getAnswers());
		}
		
		return builder.toHashCode();
	}
	
}
