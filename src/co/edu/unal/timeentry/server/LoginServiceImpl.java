package co.edu.unal.timeentry.server;

import co.edu.unal.timeentry.client.LoginInfo;
import co.edu.unal.timeentry.client.LoginService;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {

	@Override
	public LoginInfo login(String requesUri) {
		LoginInfo loginInfo = new LoginInfo();
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setLogoutUrl(userService.createLogoutURL(requesUri));
			loginInfo.setNickname(user.getNickname());
			loginInfo.setEmailAddress(user.getEmail());
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requesUri));
		}
		return loginInfo;
	}
}