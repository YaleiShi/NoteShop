package frontEnd;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dataBase.GreatDataBase;

/**
 * the servlet which handle the buy ticket action
 * @author yalei
 *
 */
public class BuyServlet extends BaseServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		super.checkLogin(request, response);
		String eventId = request.getParameter("eventId");
		
		PrintWriter out = prepareResponse(response);
		out.println(header("Buy Page", request));
		
		out.println(formHeader("/buy"));
		out.println("<br>Event Id: <br><input type=\"text\" name=\"eventId\" value=\"" + eventId + "\"/> ");
		out.println(formBody("How many: ", "text", "num"));
		out.println(formFooter());
		out.println(footer());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		super.checkLogin(request, response);
		GreatDataBase gdb = (GreatDataBase) getServletConfig().getServletContext().getAttribute(DATABASE);
		String userId = (String) request.getSession().getAttribute(ACCOUNT);
		String eventId = request.getParameter("eventId");
		int num = Integer.parseInt(request.getParameter("num"));
		
		PrintWriter out = prepareResponse(response);
		out.println(header("Buy Page", request));
		if(gdb.buyTickets(userId, eventId, num)) {
			out.println("<p>Succeed!<p>");
		}else{
			out.println("<p>failed!<p>");
		}
		out.println("<a href=\"buy?eventId=" + eventId + "\" style=" + ButtonStyle + ">Back</a>");
		out.println(footer());
	}
}
