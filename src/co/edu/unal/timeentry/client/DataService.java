package co.edu.unal.timeentry.client;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService {
	String[] getProjects();
	String[] getMilestones(String project);
	String addEntries(Vector<TimeEntryData> entries) throws NotLoggedInException;
	Vector<TimeEntryData> getEntries() throws NotLoggedInException;
}
