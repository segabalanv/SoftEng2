package co.edu.unal.timeentry.server.guice.ofy;

import co.edu.unal.timeentry.server.guice.SystemServiceServletModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class OfyServletContextListener extends GuiceServletContextListener {

	private final Injector injector = Guice.createInjector(
			new OfyBussinessModule(), new SystemServiceServletModule());
	
	@Override
	protected Injector getInjector() {
		return injector;
	}
	
}
