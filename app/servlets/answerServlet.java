package app.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.google.gson.Gson;

import app.AppConstants;
import app.model.AnswerContent;
import app.model.QuestionContent;

/**
 * Servlet implementation class answerServlet
 */
@WebServlet("/answerServlet")
public class answerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public answerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ResultSet rs =null;
		PreparedStatement pstmt=null;
		Connection conn = null;
		PrintWriter out=null;

		try {


			ArrayList <AnswerContent> messageResult= new ArrayList <AnswerContent>();
			Gson gson = new Gson();
			String jsonResult;
			response.setContentType("text/html");
			out = response.getWriter();	

			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(AppConstants.DB_DATASOURCE);
			conn = ds.getConnection();

			//checkSession
			//HttpSession session=request.getSession(false); //Prevent access to SRV if user does not have an active session
			//if (session ==null) {
			//	return ;
			//}

			//String username = (String)session.getAttribute("username");
			//String onlyfollowed = request.getParameter("onlyfollowed");

			/*if (onlyfollowed != null && onlyfollowed.contains("true")) {
				pstmt = conn.prepareStatement(AppConstants.SELECT_TOP_MESSAGES_BY_POPULARITY_OF_USER_I_FOLLOW);
				pstmt.setString(1, username);
				pstmt.setString(2, username);
				pstmt.setInt(3, 10);
			} else {
				pstmt = conn.prepareStatement(AppConstants.SELECT_TOP_MESSAGES_BY_POPULARITY);
				pstmt.setString(1, username);
				pstmt.setString(2, username);
				pstmt.setInt(3, 10);
			}
			*/
			//int offset = Integer.parseInt(request.getParameter("offset"));
			pstmt = conn.prepareStatement(AppConstants.SELECT_ANSWERS);
			int qid = Integer.parseInt(request.getParameter("qid"));
			pstmt.setInt(1, qid);
			rs = pstmt.executeQuery();
			
			long tsTime;
			DateFormat df;
			Date startDate;
			String createdDate;
			
			while(rs.next()){
				tsTime = rs.getTimestamp(1).getTime();
				df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				startDate = new Date(tsTime);
				createdDate = df.format(startDate);
				//modify date format
				AnswerContent qC = new AnswerContent ( createdDate, rs.getString(2), rs.getString(3), rs.getFloat(4),rs.getInt(5));
				messageResult.add(qC);

			}
			jsonResult=gson.toJson(messageResult,ArrayList.class);
			out.print(jsonResult);
			

		} catch (IOException | SQLException | NamingException e) {
			getServletContext().log("Error ,general SQLException ", e);
		} finally {
			try { rs.close(); } catch (Exception e) { /* ignored */ }
			try { pstmt.close(); } catch (Exception e) { /* ignored */ }
			try { conn.close(); } catch (Exception e) { /* ignored */ }
			try { out.close(); } catch (Exception e) { /* ignored */ }


		}
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
			resSet = statement.executeQuery(AppConstants.A_MAX_ID);
			if(resSet.next() == true)
			{
				id = resSet.getInt(1)+1;
			} 
			connection.commit();
			statement.close();
			
			pStatement = connection.prepareStatement(AppConstants.INSERT_ANSWER_STMT);
			pStatement.setInt ( 1 , id);
			pStatement.setInt ( 2 , Integer.parseInt(request.getParameter("id")));
			pStatement.setFloat ( 3 , 0);
			pStatement.setString( 4 , request.getParameter("answer"));
			pStatement.setString( 5 , request.getParameter("nickname"));
			pStatement.setTimestamp ( 6 , timestamp);
		
			pStatement.executeUpdate();
			connection.commit();
			
			//update questions table
			pStatement.close();
			pStatement = connection.prepareStatement(AppConstants.UPDATE_Q_TABLE);
			pStatement.setInt ( 1 , Integer.parseInt(request.getParameter("id")));
			pStatement.executeUpdate();
			connection.commit();
			
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
