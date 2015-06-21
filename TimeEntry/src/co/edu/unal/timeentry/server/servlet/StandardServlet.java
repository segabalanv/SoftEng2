package co.edu.unal.timeentry.server.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

public class StandardServlet extends HttpServlet {
	
	public static final String PARAM = "param";
	
	public void init(final ServletConfig sc) {
		try {
			super.init(sc);
			String param = sc.getInitParameter(PARAM);
			System.out.println(PARAM + "=" + param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
