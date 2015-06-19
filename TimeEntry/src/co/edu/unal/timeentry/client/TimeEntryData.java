package co.edu.unal.timeentry.client;

import java.io.Serializable;
import java.util.Date;

public class TimeEntryData implements Serializable {
	
	private String project;
	private String milestone;
	private Boolean billable;
	private Date date;
	private double hours;
	
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getMilestone() {
		return milestone;
	}
	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getHours() {
		return hours;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}

}