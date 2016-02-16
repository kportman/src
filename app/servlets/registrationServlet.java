package app.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import app.AppConstants;

/**
 * Servlet implementation class registrationServlet
 */
public class registrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public registrationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			
			Context context = new InitialContext();
			BasicDataSource dataSource = (BasicDataSource) context.lookup(AppConstants.DB_DATASOURCE);
			connection = dataSource.getConnection();
			
			statement = connection.prepareStatement(AppConstants.INSERT_USER_STMT);
			statement.setString( 1 , request.getParameter("username"));
			statement.setString( 2 , request.getParameter("password"));
			statement.setString( 3 , request.getParameter("nickname"));
			statement.setString( 4 , request.getParameter("description"));
			statement.setString( 5 , request.getParameter("imageLink"));
			statement.setInt(6, 0);
			
			statement.executeUpdate();
			
			connection.commit();
			
			response.setContentType("text/html");
			response.getWriter().print("Ok");
			System.out.println("Ok");
			
			
			
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
