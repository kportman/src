package app.listeners;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import app.AppConstants;

/**
 * Application Lifecycle Listener implementation class FunItalyContextListener
 *
 */
public class FunItalyContextListener implements ServletContextListener {
	
    /**
     * Default constructor. 
     */
    public FunItalyContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

    
    
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	Connection connection = null;
		Statement statement = null;
		ResultSet resSet = null;
		
		try {
				Context context = new InitialContext();
				BasicDataSource dataSource = (BasicDataSource) context.lookup(AppConstants.DB_DATASOURCE);
				connection = dataSource.getConnection();
				DatabaseMetaData dbmd = connection.getMetaData();

				resSet = dbmd.getTables(null, null, "USERS", null);
				if (resSet.next() == false) {
					statement = connection.createStatement();
					statement.executeUpdate(AppConstants.CREATE_USER_TABLE);
					connection.commit();
					statement.close();
				}
				resSet.close();
				
				resSet = dbmd.getTables(null, null, "QUESTIONS", null);
				if (resSet.next() == false) {
					statement = connection.createStatement();
					statement.executeUpdate(AppConstants.CREATE_QUESTIONS_TABLE);
					connection.commit();
					statement.close();
				}
				resSet.close();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		
    	}
    }
	
}
