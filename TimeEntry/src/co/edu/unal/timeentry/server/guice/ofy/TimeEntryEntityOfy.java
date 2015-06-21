package co.edu.unal.timeentry.server.guice.ofy;

import java.util.Date;

import co.edu.unal.timeentry.server.guice.ITimeEntryData;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
public class TimeEntryEntityOfy implements ITimeEntryData {
	@Id Long id;
	@Index private String email;
	private String project;
	private String milestone;
	private Boolean billable;
	private Date date;
	private double hours;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
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