package frontEnd;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dataBase.GreatDataBase;

@SuppressWarnings("serial")
public class LoginServlet extends BaseServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = prepareResponse(response);
		out.println(simpleHeader("Login Page"));
		out.println("<p>Please Login First~</p>");
		out.println("<hr>");
		out.println(formHeader("/login"));
		out.println(formBody("Account", "text", "userId"));
		out.println(formBody("Password" , "password", "password"));
		out.println(formFooter());
		out.println("<p>If you do not have the account, you can register one.</P><br>");
		out.println("<a href=\"register\" style=" + ButtonStyle + ">Sign Up</a>");
		out.println(footer());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userId = (String) request.getParameter(USER_ID);
		String password = (String) request.getParameter(PASSWORD);
		GreatDataBase gdb = (GreatDataBase) getServletConfig().getServletContext().getAttribute(DATABASE);
		boolean ifPass = gdb.checkPass(userId, password);
		
		PrintWriter out = prepareResponse(response);
		out.println(simpleHeader("Check Password"));
		if(ifPass) {
			HttpSession session = request.getSession();
			session.setAttribute(LOGIN, "true");
			session.setAttribute(ACCOUNT, userId);
			out.println("<p>login succeed</p><hr/>");
			out.println("<a href=\"mainPage\" style=" + ButtonStyle + ">Go</a>");
		}else {
			out.println("<p>login failed, please check your password or account again</p><hr/>");
			out.println("<a href=\"login\" style=" + ButtonStyle + ">Back</a>");
		}
		out.println(footer());
	}

}
