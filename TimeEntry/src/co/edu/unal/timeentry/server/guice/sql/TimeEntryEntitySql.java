package co.edu.unal.timeentry.server.guice.sql;

import java.util.Date;

import co.edu.unal.timeentry.server.guice.ITimeEntryData;

public class TimeEntryEntitySql implements ITimeEntryData {
	
	private Long id;
	private String email;
	private String project;
	private String milestone;
	private Boolean billable;
	private Date date;
	private double hours;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;		
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;		
	}

	@Override
	public String getProject() {
		return project;
	}

	@Override
	public void setProject(String project) {
		this.project = project;		
	}

	@Override
	public String getMilestone() {
		return milestone;
	}

	@Override
	public void setMilestone(String milestone) {
		this.milestone = milestone;		
	}

	@Override
	public Boolean getBillable() {
		return billable;		
	}

	@Override
	public void setBillable(Boolean billable) {
		this.billable = billable;
		
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
		
	}

	@Override
	public double getHours() {
		return hours;
	}

	@Override
	public void setHours(double hours) {
		this.hours = hours;
		
	}

}
