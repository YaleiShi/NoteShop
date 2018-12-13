package frontEnd;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataBase.GreatDataBase;

public class UpdateEventServlet extends BaseServlet{

	/**
	 * provide the update event page to the users
	 * to fill in the message they want to modify
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		super.checkLogin(request, response);
		String eventId = request.getParameter("eventId");
		
		PrintWriter out = prepareResponse(response);
		out.println(header("Update Event Page", request));
		
		out.println(formHeader("/updateEvent"));
		out.println("<br>Event Id: " + eventId + "<br><input type=\"hidden\" name=\"eventId\" value=\"" + eventId + "\"/> ");
		out.println(formBody("Event Name: ", "text", "eventName"));
		out.println(formBody("Date: ", "text", "date"));
		out.println(formBody("How Many Tickets: ", "text", "num"));
		out.println(formBody("Description: ", "text", "detail"));
		out.println(formFooter());
		out.println(footer());
	}
	
	/**
	 * call the update event method in the data manager
	 * it will check the user id to match the event creator
	 * then try to update the event
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		super.checkLogin(request, response);
		GreatDataBase gdb = (GreatDataBase) getServletConfig().getServletContext().getAttribute(DATABASE);
		String userId = (String) request.getSession().getAttribute(ACCOUNT);
		String eventId = request.getParameter("eventId");
		String eventName = request.getParameter("eventName");
		String date = request.getParameter("date");
		int num = Integer.parseInt(request.getParameter("num"));
		String description = request.getParameter("detail");
		
		PrintWriter out = prepareResponse(response);
		out.println(header("Update Event Page", request));
		if(gdb.updateEvent(userId, eventId, eventName, date, num, description)) {
			out.println("<p>Succeed!<p>");
		}else{
			out.println("<p>failed!<p>");
		}
		out.println("<a href=\"updateEvent?eventId=" + eventId + "\" style=" + ButtonStyle + ">Back</a>");
		out.println(footer());
	}
}
