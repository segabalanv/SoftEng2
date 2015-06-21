package co.edu.unal.timeentry.server;

import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import co.edu.unal.timeentry.client.DataService;
import co.edu.unal.timeentry.client.NotLoggedInException;
import co.edu.unal.timeentry.client.TimeEntryData;
import co.edu.unal.timeentry.server.guice.ofy.TimeEntryEntityOfy;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;

import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements DataService {

	private static final Logger LOG = Logger.getLogger(DataServiceImpl.class.getName());
	
	@Override
	public void init(ServletConfig sc) {
		try {
			super.init(sc);
			ObjectifyService.register(TimeEntryEntityOfy.class);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	// returns a String Array of project names 
	@Override
	public String[] getProjects() {
		String[] projects = new String[3];
		projects[0] = "Proyecto 1";
		projects[1] = "Proyecto 2";
		projects[2] = "Proyecto 3";
		
		return projects;
	}

	// returns a simple String Array of milestone name for a project
	@Override
	public String[] getMilestones(String project) {
		String[] milestones = new String[3];
		if(project.equals("Proyecto 1")) {
			milestones[0] = "Objetivo 1-1";
			milestones[1] = "Objetivo 1-2";
			milestones[2] = "Objetivo 1-3";
		} else if(project.equals("Proyecto 2")) {
			milestones[0] = "Objetivo 2-1";
			milestones[1] = "Objetivo 2-2";
			milestones[2] = "Objetivo 2-3";
		} else {
			milestones[0] = "Objetivo 3-1";
			milestones[1] = "Objetivo 3-2";
			milestones[2] = "Objetivo 3-3";
		}
		return milestones;
	}

	@Override
	public String addEntries(Vector<TimeEntryData> entries)
			throws NotLoggedInException {
		// ensure that the curent user is logged in
		checkLoggedIn();
		ofy().save().entities(toEntities(entries)).now();
		LOG.log(Level.INFO, entries.size() + " entries added.");
		return entries.size() + " entries added";
	}

	@Override
	public Vector<TimeEntryData> getEntries() throws NotLoggedInException {
		// ensure that the current user is logged in
		checkLoggedIn();
		
		Vector<TimeEntryData> entries = new Vector<TimeEntryData>();
		
		List<TimeEntryEntityOfy> entities = ofy().load().type(TimeEntryEntityOfy.class).
				filter("email",getUser().getEmail()).list();
		
		for(TimeEntryEntityOfy entity : entities) {
			TimeEntryData ted = new TimeEntryData();
			ted.setBillable(entity.getBillable());
			ted.setDate(entity.getDate());
			ted.setHours(entity.getHours());
			ted.setMilestone(entity.getMilestone());
			ted.setProject(entity.getProject());
			entries.add(ted);
		}
		return entries;
	}
	
	// return the current user from Google Accounts
	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}
	
	// determines if user is currently logged in. If not, throws an exception
	private void checkLoggedIn() throws NotLoggedInException {
		if(getUser() == null)
			throw new NotLoggedInException("User not logged in. Please login with your Google"
					+ " Accounts credentials");
	}
	
	// utility method to translate client object to server-side objects
	private Vector<TimeEntryEntityOfy> toEntities(Vector<TimeEntryData> entries) {
		// create a new vector of Entities to return
		Vector<TimeEntryEntityOfy> entities = new Vector<TimeEntryEntityOfy>();
		for(int i=0; i<entries.size(); i++) {
			TimeEntryData ted = (TimeEntryData)entries.get(i);
			TimeEntryEntityOfy tee = new TimeEntryEntityOfy();
			tee.setBillable(ted.getBillable());
			tee.setDate(ted.getDate());
			tee.setHours(ted.getHours());
			tee.setMilestone(ted.getMilestone());
			tee.setProject(ted.getProject());
			tee.setEmail(getUser().getEmail());
			entities.add(tee);
		}
		return entities;
	}
	
}