package frontEnd;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends BaseServlet{
	
	/**
	 * set the user's session to be not login
	 * and set the account session of the request to null
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		session.setAttribute(LOGIN, "false");
		session.setAttribute(ACCOUNT, null);
		response.sendRedirect(response.encodeRedirectURL("/login"));
	}

}
