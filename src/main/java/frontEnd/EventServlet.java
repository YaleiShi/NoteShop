package frontEnd;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataBase.GreatDataBase;

public class EventServlet extends BaseServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		super.checkLogin(request, response);
		
		GreatDataBase gdb = (GreatDataBase) getServletConfig().getServletContext().getAttribute(DATABASE);
		String eventId = request.getParameter("eventId");
		ResultSet res;
		
		PrintWriter out = prepareResponse(response);
		out.println(header("Event Detail", request));
		
		try {
			res = gdb.getEventData("eventId", eventId);
			while(res.next()) {
				String eventName = res.getString("eventName");
				String date = res.getString("date");
				int numTickets = res.getInt("numTickets");
				String description = res.getString("description");
				out.println("<p>Event Id: " + eventId + "</p>");
				out.println("<p>Event Name: " + eventName + "</p>");
				out.println("<p>Date: " + date + "</p>");
				out.println("<p>Rest of Tickets: " + numTickets + "</p>");
				out.println("<p>Description: " + description +"</p>");
				out.println("<a href=\"buy?eventId=" + eventId + "\" style=" + ButtonStyle + ">Buy Tickets</a>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		out.println(footer());
		
	}

}
