package frontEnd;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Base class for all Servlets in this application.
 * Provides general helper methods.
 */
public class BaseServlet extends HttpServlet {
	
	public static final String DATABASE = "dataBase";
	public static final String LOGIN = "login";
	public static final String USER_ID = "userId";
	public static final String PASSWORD = "password";
	public static final String ACCOUNT = "account";
	public static final String CanDecidePass = "canPass";
	public static final String CanAddUser = "canAdd";
	public static final String TableStyle = "<table border=2 border-spacing=3px style=\"width:50%\"><style>\r\n" + 
			"table {\r\n" + 
			"    font-family: arial, sans-serif;\r\n" + 
			"    width: 100%;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"td, th {\r\n" + 
			"    border: 1px solid #dddddd;\r\n" + 
			"    text-align: left;\r\n" + 
			"    padding: 8px;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"tr:nth-child(even) {\r\n" + 
			"    background-color: rgb(170, 203, 255);\r\n" + 
			"}\r\n" + 
			"</style>";
	public static final String ButtonStyle = "\"border:1px solid #ccc;text-decoration:unset;padding:1px 20px;color:#000;background:#eee\"";
	public static final String DELETE = "delete";
	public static final String STATUS = "status";
	public static final String ERROR = "error";
	public static final String NOT_LOGGED_IN = "not_logged_in";
	public static final String CONTENT = "content"; 
	/*
	 * Prepare a response of HTML 200 - OK.
	 * Set the content type and status.
	 * Return the PrintWriter.
	 */
	protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		return response.getWriter();

	}
	
	protected String simpleHeader(String title) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head><title>" + title + "</title></head><body>");
		sb.append("<center>");
		return sb.toString();
	}
	
	/**
	 * prepare a simple form for the html page
	 * @param path
	 * @param query
	 * @return
	 */
	protected String formHeader(String path) {
		return "<form action=\"" + path + "\" method=\"post\">";
	}
	
	protected String formBody(String title, String type, String query) {
		return "<br>" + title + " : <br>" + "<input type=\"" + type + "\" name=\"" + query + "\"/> ";
	}
	
	protected String formFooter() {
		return "<br><input type=\"submit\" value=\"Submit\"/></form>";
	}
	
	/*
	 * Return the beginning part of the HTML page.
	 */
	protected String header(String title, HttpServletRequest request) {
		HttpSession session = request.getSession();
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head><title>" + title + "</title>"
				+ "</head><body>");
		sb.append("<center>");
		sb.append("<h1>This is Ticket Purchase APP</h1>");
		sb.append("<a href=\"mainPage\" style=" + ButtonStyle + ">Main Page</a>");
		sb.append("<a href=\"userInfo\" style=" + ButtonStyle + ">User Info & Tickets</a>");
		sb.append("<a href=\"eventAdmin\" style=" + ButtonStyle + ">Event Interface</a>");
		sb.append("<a href=\"logout\" style=" + ButtonStyle + ">Logout</a>");
		sb.append("<hr><hr>");
		return sb.toString();		
	}
	
	/*
	 * Return the last part of the HTML page. 
	 */
	protected String footer() {
		return "</center></body></html>";
	}
	
	/**
	 * check the login status
	 * @param request
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	protected void checkLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String login = (String) session.getAttribute(LOGIN);
		if(login == null || !login.equals("true")) {
			response.sendRedirect(response.encodeRedirectURL("/login"));
			return;
		}
	}
	
	/*
	 * Given a request, return the name found in the 
	 * Cookies provided.
	 */
	protected String getCookieValue(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();

		String name = null;
		
		if(cookies != null) {
			//for each cookie, if the key is name, store the value
			for(Cookie c: cookies) {
				if(c.getName().equals(key)) {
					name = c.getValue();
				}
			}
		}
		return name;
	}
	
	/*
	 * Given a request, return the value of the parameter with the
	 * provided name or null if none exists.
	 */
	protected String getParameterValue(HttpServletRequest request, String key) {
		return request.getParameter(key);
	}
			
}
