package frontEnd;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataBase.GreatDataBase;

public class CreateEventServlet extends BaseServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		super.checkLogin(request, response);
		
		PrintWriter out = prepareResponse(response);
		out.println(header("Create Event Page", request));
		
		out.println(formHeader("/createEvent"));
		out.println(formBody("Event Id (unique): ", "text", "eventId"));
		out.println(formBody("Event Name: ", "text", "eventName"));
		out.println(formBody("Date: ", "text", "date"));
		out.println(formBody("How Many Tickets: ", "text", "num"));
		out.println(formBody("Description: ", "text", "detail"));
		out.println(formFooter());
		out.println(footer());
	}
	
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
		out.println(header("Create Event Page", request));
		if(gdb.createEvents(eventId, eventName, date, num, description, userId)) {
			out.println("<p>Succeed!<p>");
		}else{
			out.println("<p>failed!<p>");
		}
		out.println("<a href=\"createEvent\" style=" + ButtonStyle + ">Back</a>");
		out.println(footer());
	}

}
