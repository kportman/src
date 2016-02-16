package app.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

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
 * Servlet implementation class questionServlet
 */
@WebServlet("/questionServlet")
public class questionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public questionServlet() {
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
		PreparedStatement pStatement = null;
		Statement statement = null;
		ResultSet resSet = null;
		
		Date date=new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		int id = 1;
		try {
			
			Context context = new InitialContext();
			BasicDataSource dataSource = (BasicDataSource) context.lookup(AppConstants.DB_DATASOURCE);
			connection = dataSource.getConnection();
			
			statement = connection.createStatement();
			resSet = statement.executeQuery(AppConstants.Q_MAX_ID);
			if(resSet.next() == true)
			{
				id = resSet.getInt(1)+1;
			} 
			connection.commit();
			statement.close();
			
			pStatement = connection.prepareStatement(AppConstants.INSERT_QUESTION_STMT);
			pStatement.setInt ( 1 , id);
			pStatement.setInt ( 2 , 0);
			pStatement.setInt ( 3 , 0);
			pStatement.setString( 4 , request.getParameter("question"));
			pStatement.setString( 5 , request.getParameter("nickname"));
			pStatement.setTimestamp ( 6 , timestamp);
			pStatement.setInt( 7, 0);
			pStatement.setString(8, request.getParameter("topic"));
			pStatement.executeUpdate();
			connection.commit();
			
			response.setContentType("text/html");
			response.getWriter().print("Ok");
			System.out.println("Ok");
			
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				pStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
