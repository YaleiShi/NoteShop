package frontEnd;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataBase.GreatDataBase;

public class UserInfoServlet extends BaseServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		super.checkLogin(request, response);
		
		GreatDataBase gdb = (GreatDataBase) getServletConfig().getServletContext().getAttribute(DATABASE);
		String user = (String) request.getSession().getAttribute(ACCOUNT);
		ResultSet res;
		
		PrintWriter out = prepareResponse(response);
		out.println(header("User Info", request));
		
		try {
			res = gdb.getUserData("userId", user);
			while(res.next()) {
				String firstName = res.getString("firstName");
				String lastName = res.getString("lastName");
				String detail = res.getString("detail");
				out.println("<p>User Id: " + user + "</p>");
				out.println("<p>First Name: " + firstName + "</p>");
				out.println("<p>Last Name: " + lastName + "</p>");
				out.println("<p>Detail: " + detail +"</p>");
			}
			
			res = gdb.getUserEvent(user);
			out.println(TableStyle);
			out.println("<tr><th>Ticket Id</th>"
					+ "<th>Event Id</th>"
					+ "<th>Event Name</th>"
					+ "<th>Date</th>"
					+ "<th>Transaction</th></tr>");
			while(res.next()) {
				int ticketId = res.getInt("ticketId");
				String eventId = res.getString("eventId");
				String eventName = res.getString("eventName");
				String date = res.getString("date");
				out.println("<tr><td>" + ticketId + "</td>"
						  + "<td><a href=\"event?eventId=" + eventId + "\">" + eventId + "</a></td>"
					      + "<td>" + eventName + "</td>"
					      + "<td>" + date + "</td>"
					      + "<td><a href=\"send?ticketId=" + ticketId + "\">Send</a></td></tr>");
			}
			out.println("</table>");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		out.println(footer());
		
	}

}
