package frontEnd;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import dataBase.GreatDataBase;

/**
 * the server starter of the application
 * @author yalei
 *
 */
public class AppServer {
	
	/**
	 * start the server and put all the servlet into the
	 * servlet handler
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Server server = new Server(7070);
        
        //create a ServletHander to attach servlets
        ServletContextHandler servhandler = new ServletContextHandler(ServletContextHandler.SESSIONS);        
        server.setHandler(servhandler);
        
        servhandler.addEventListener(new ServletContextListener() {

        	public void contextDestroyed(ServletContextEvent sce) {
				// TODO Auto-generated method stub
				
			}
        		
			public void contextInitialized(ServletContextEvent sce) {
				try {
					GreatDataBase gdb = GreatDataBase.getInstance();
					sce.getServletContext().setAttribute(BaseServlet.DATABASE, gdb);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Data base start ----- failed");
					e.printStackTrace();
					System.exit(1);
				}
				
				
			}
        	
        });
        
        servhandler.addServlet(LoginServlet.class, "/login");
        servhandler.addServlet(RegisterServlet.class, "/register");
        servhandler.addServlet(LogoutServlet.class, "/logout");
        servhandler.addServlet(MainPageServlet.class, "/mainPage");
        servhandler.addServlet(UserInfoServlet.class, "/userInfo");
        servhandler.addServlet(EventServlet.class, "/event");
        servhandler.addServlet(BuyServlet.class, "/buy");
        servhandler.addServlet(EventInterfaceServlet.class, "/eventAdmin");
        servhandler.addServlet(CreateEventServlet.class, "/createEvent");
        servhandler.addServlet(UpdateEventServlet.class, "/updateEvent");
        servhandler.addServlet(TransactionServlet.class, "/send");
        servhandler.addServlet(SearchServlet.class, "/search");
        
        //set the list of handlers for the server
        server.setHandler(servhandler);
        
        //start the server
        server.start();
        server.join();
	}
}
