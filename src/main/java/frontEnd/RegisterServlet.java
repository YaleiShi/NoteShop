package frontEnd;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dataBase.GreatDataBase;

public class RegisterServlet extends BaseServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter out = prepareResponse(response);
		out.println(simpleHeader("Decide your account name~"));
		out.println("<center>");
		out.println("<p>Choose a great name as your account name~</p>");
		out.println("<hr>");
		out.println(formHeader("/register"));
		out.println(formBody("User Id", "text", "userId"));
		out.println(formBody("Password", "password", "password"));
		out.println(formBody("Confirm Password", "password", "confirm"));
		out.println(formBody("First Name", "text", "firstName"));
		out.println(formBody("Last Name", "text", "lastName"));
		out.println(formBody("Introduce yourself", "text", "detail"));
		out.println(formFooter());
		out.println("<a href=\"login\" style=" + ButtonStyle + ">Back to Login</a>");
		out.println(footer());	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userId = (String) request.getParameter(USER_ID);
		String password = (String) request.getParameter(PASSWORD);
		String confirm = (String) request.getParameter("confirm");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String detail = request.getParameter("detail");
		GreatDataBase gdb = (GreatDataBase) getServletConfig().getServletContext().getAttribute(DATABASE);
		
		boolean ifUserExist = gdb.ifUserExist(userId);
		boolean ifPasswordMatch = password.equals(confirm);
		
		PrintWriter out = prepareResponse(response);
		out.println(simpleHeader("Check Register"));
		if(ifUserExist) {
			out.println("<p>User Name Exist</p><hr/>");
			out.println("<a href=\"register\" style=" + ButtonStyle + ">Back</a>");
		}else {
			if(!ifPasswordMatch) {
				out.println("<p>Password Not Match</p><hr/>");
				out.println("<a href=\"register\" style=" + ButtonStyle + ">Back</a>");
			}else {
				HttpSession session = request.getSession();
				session.setAttribute(LOGIN, "true");
				session.setAttribute(ACCOUNT, userId);
				try {
					gdb.saveUserData(userId, password, firstName, lastName, detail);
					out.println("<p>login succeed</p><hr/>");
					out.println("<a href=\"mainPage\" style=" + ButtonStyle + ">Go</a>");
				} catch (SQLException e) {
					e.printStackTrace();
					out.println("<p>Data Save .... Failed, Please Try Another ID or Password</p><hr/>");
					out.println("<a href=\"register\" style=" + ButtonStyle + ">Back</a>");
				}
			}
		}
		out.println(footer());
		
		
		
	}

}
