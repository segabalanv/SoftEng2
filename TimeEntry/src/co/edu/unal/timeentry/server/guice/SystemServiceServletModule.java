package co.edu.unal.timeentry.server.guice;

import java.util.Map;

import co.edu.unal.timeentry.server.servlet.StandardServlet;

import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.inject.Module;

public class SystemServiceServletModule extends GuiceSystemServiceServletModule 
	implements Module {
	
	protected void configureServlets() {
		super.configureServlets();
		
		Map<String, String> params = new HashMap<>();
		params.put(StandardServlet.PARAM, "param");
		
		serve("/standard").with(StandardServlet.class, params);
	}
	
}
