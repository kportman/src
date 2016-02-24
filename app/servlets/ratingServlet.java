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
 * Servlet implementation class ratingServlet
 */
@WebServlet("/ratingServlet")
public class ratingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ratingServlet() {
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
		
		//Date date=new Date();
		//Timestamp timestamp = new Timestamp(date.getTime());
		
		//int id = 1;
		try {
			
			Context context = new InitialContext();
			BasicDataSource dataSource = (BasicDataSource) context.lookup(AppConstants.DB_DATASOURCE);
			connection = dataSource.getConnection();
			
			//statement = connection.createStatement();
			//resSet = statement.executeQuery(AppConstants.A_MAX_ID);
			 
			//connection.commit();
			//statement.close();
			
			if(request.getParameter("voteType").equals("answer"))
			{
				pStatement = connection.prepareStatement(AppConstants.VOTE_ANSWER_STMT);
				pStatement.setInt ( 1 , Integer.parseInt(request.getParameter("vote")));
				pStatement.setInt ( 2 , Integer.parseInt(request.getParameter("ansID")));
				pStatement.executeUpdate();
				connection.commit();
				pStatement.close();
				//update questions table
				pStatement = connection.prepareStatement(AppConstants.VOTE_ANS_UPDATE_QUES_STMT );
				pStatement.setInt ( 1 , Integer.parseInt(request.getParameter("vote")));
				pStatement.setInt ( 2 , Integer.parseInt(request.getParameter("vote")));
				pStatement.setInt ( 3 , Integer.parseInt(request.getParameter("quesID")));
				pStatement.executeUpdate();
				connection.commit();
				
			} else if(request.getParameter("voteType").equals("question")){
				pStatement = connection.prepareStatement(AppConstants.VOTE_QUESTION_STMT);
				pStatement.setInt ( 1 , Integer.parseInt(request.getParameter("vote")));
				pStatement.setInt ( 2 , Integer.parseInt(request.getParameter("vote")));
				pStatement.setInt ( 3 , Integer.parseInt(request.getParameter("vote")));
				pStatement.setInt ( 4 , Integer.parseInt(request.getParameter("quesID")));
				pStatement.executeUpdate();
				connection.commit();
				
			}
			
			//update USERS table
			pStatement.close();
			pStatement = connection.prepareStatement(AppConstants.UPDATE_USERS_TABLE);
			pStatement.setString( 1 , request.getParameter("nickname"));
			pStatement.setString( 2 , request.getParameter("nickname"));
			pStatement.setString( 3 , request.getParameter("nickname"));
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
