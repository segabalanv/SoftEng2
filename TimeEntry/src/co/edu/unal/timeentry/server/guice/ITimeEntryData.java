package co.edu.unal.timeentry.server.guice;

import java.util.Date;

public interface ITimeEntryData {
	
	Long getId();
	void setId(Long id);
	
	String getEmail();
	void setEmail(String email);
	
	String getProject();
	void setProject(String project);
	
	String getMilestone();
	void setMilestone(String milestone);
	
	Boolean getBillable();
	void setBillable(Boolean billable);
	
	Date getDate();
	void setDate(Date date);
	
	double getHours();
	void setHours(double hours);

}