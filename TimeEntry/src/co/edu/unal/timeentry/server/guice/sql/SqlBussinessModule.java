package co.edu.unal.timeentry.server.guice.sql;

import co.edu.unal.timeentry.server.guice.ITimeEntryData;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class SqlBussinessModule extends AbstractModule implements Module {

	@Override
	protected void configure() {
		bind(ITimeEntryData.class).to(TimeEntryEntitySql.class);		
	}
	
}
