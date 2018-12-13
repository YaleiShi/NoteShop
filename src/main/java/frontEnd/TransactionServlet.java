package frontEnd;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataBase.GreatDataBase;

public class TransactionServlet extends BaseServlet{

	/**
	 * display the transaction form to the users 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		super.checkLogin(request, response);
		String ticketId = request.getParameter("ticketId");
		
		PrintWriter out = prepareResponse(response);
		out.println(header("Transaction Page", request));
		
		out.println(formHeader("/send"));
		out.println("<br>Ticket Id: <br><input type=\"text\" name=\"ticketId\" value=\"" + ticketId + "\"/> ");
		out.println(formBody("Target UserId: ", "text", "target"));
		out.println(formFooter());
		out.println(footer());
	}
	
	/**
	 * call the transaction method in the data manager
	 * return success page if succeed
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		super.checkLogin(request, response);
		GreatDataBase gdb = (GreatDataBase) getServletConfig().getServletContext().getAttribute(DATABASE);
		String userId = (String) request.getSession().getAttribute(ACCOUNT);
		String targetId = request.getParameter("target");
		int ticketId = Integer.parseInt(request.getParameter("ticketId"));
		
		PrintWriter out = prepareResponse(response);
		out.println(header("Transaction Page", request));
		if(gdb.transaction(userId, targetId, ticketId)) {
			out.println("<p>Succeed!<p>");
		}else{
			out.println("<p>Failed, target or ticket not exist, or you don't have this ticket.<p>");
		}
		out.println("<a href=\"send?ticketId=" + ticketId + "\" style=" + ButtonStyle + ">Back</a>");
		out.println(footer());
	}
}
